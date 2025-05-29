package org.example.safetyconnection.service.yolov8;

import ai.djl.Application;
import ai.djl.inference.Predictor;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.output.DetectedObjects;
import ai.djl.modality.cv.output.Rectangle;
import ai.djl.modality.cv.transform.ToTensor;
import ai.djl.modality.cv.translator.YoloV8Translator;
import ai.djl.opencv.OpenCVImageFactory;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.translate.Translator;
import org.example.safetyconnection.dto.request.DetectedObjectRequestDTO;
import org.example.safetyconnection.dto.response.DetectedObjectResponseDTO;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Yolov8DetectionSerivce {
    private final OpenCVImageFactory imageFactory = new OpenCVImageFactory();
    private final Translator<Image, DetectedObjects> translator = YoloV8Translator.builder()
            .addTransform(new ToTensor())
            .optThreshold(0.25f)
            .optNmsThreshold(0.45f)
            .build();

    private final Criteria<Image, DetectedObjects> criteria = Criteria.builder()
            .setTypes(Image.class, DetectedObjects.class)
            .optApplication(Application.CV.OBJECT_DETECTION)
            .optEngine("PyTorch")
            .optModelUrls("https://github.com/SafetyConnection2025/SafetyConnection-Server/releases/download/yolov8/best.zip")
            .optTranslator(translator)
            .build();

    public DetectedObjectResponseDTO detect(DetectedObjectRequestDTO detectedObjectRequestDTO) {
        VideoCapture cap = new VideoCapture(detectedObjectRequestDTO.filename());

        if (!cap.isOpened()) {
            throw new RuntimeException("Video capture not opened");
        }
        double fps = cap.get(Videoio.CAP_PROP_FPS);
        double width = cap.get(Videoio.CAP_PROP_FRAME_WIDTH);
        double height = cap.get(Videoio.CAP_PROP_FRAME_HEIGHT);

        Mat frame = new Mat();
        MatOfByte mob = new MatOfByte();
        String message = "탐지된 무기가 없습니다";
        try (ZooModel<Image, DetectedObjects> model = criteria.loadModel(); Predictor<Image, DetectedObjects> predictor = model.newPredictor()) {
            // 프레임 단위로 영상처리
            while (cap.read(frame)) {
                Mat dst = new Mat();
                Imgproc.resize(frame, dst, new Size(640, 640));
                Image image = imageFactory.fromImage(dst);
                DetectedObjects detections = predictor.predict(image);
                // 프레임에 객체 탐지 결과 표시
                if (detections.getNumberOfObjects() == 0) {
                    continue;
                } else {
                    System.out.println("객체 탐지");
                    DetectedObjects.DetectedObject obj = detections.best();
                    Rectangle r = obj.getBoundingBox().getBounds();
                    int x = (int) (r.getX());
                    int y = (int) (r.getY());
                    int w = (int) (r.getWidth());
                    int h = (int) (r.getHeight());
                    Imgproc.rectangle(frame,
                            new Point(x, y),
                            new Point(x + w, y + h),
                            new Scalar(0, 255, 0),
                            2
                    );
                    String label = String.format("%s: %.1f%%", obj.getClassName(), obj.getProbability() * 100);
                    Imgproc.putText(
                            dst,
                            label,
                            new Point(x, y - 5),
                            Imgproc.FONT_HERSHEY_SIMPLEX,
                            0.5,
                            new Scalar(0, 255, 0),
                            2
                    );
                    Imgcodecs.imencode(".png", dst, mob);
                    message = obj.getClassName();
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Object detection failed: " + e.getMessage());
        } finally {
            cap.release();
        }
        return new DetectedObjectResponseDTO(mob.toArray(), message);
    }
}
