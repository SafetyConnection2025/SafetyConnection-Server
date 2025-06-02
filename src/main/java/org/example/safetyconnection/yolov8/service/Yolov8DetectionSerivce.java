package org.example.safetyconnection.yolov8.service;

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
import lombok.extern.slf4j.Slf4j;
import org.example.safetyconnection.yolov8.dto.request.DetectedObjectRequestDTO;
import org.example.safetyconnection.yolov8.dto.response.DetectedObjectResponseDTO;
import org.example.safetyconnection.common.exception.ObjectDetectionFailedException;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Slf4j
@Service
public class Yolov8DetectionSerivce {
    private final OpenCVImageFactory imageFactory = new OpenCVImageFactory();
    private final Translator<Image, DetectedObjects> translator = YoloV8Translator.builder()
            .addTransform(new ToTensor())
            .optThreshold(0.40f)
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
           log.info("cap.isOpened");
           // throw new FileNotFoundException(detectedObjectRequestDTO.filename());
        }

        Mat frame = new Mat();
        MatOfByte mob = new MatOfByte();
        byte[] imageBytes;
        try (ZooModel<Image, DetectedObjects> model = criteria.loadModel(); Predictor<Image, DetectedObjects> predictor = model.newPredictor()) {
            // 프레임 단위로 영상처리
            while (cap.read(frame)) {
                Mat dst = new Mat();
                Imgproc.resize(frame, dst, new Size(640, 640));
                Image image = imageFactory.fromImage(dst);
                DetectedObjects detections = predictor.predict(image);

                if (detections.getNumberOfObjects() == 0) { // 객체가 탐지되지 않은 경우 다음 프레임으로 넘어감
                    continue;
                } else {
                    // 프레임에 객체 탐지 결과 표시
                    DetectedObjects.DetectedObject obj = detections.best();
                    System.out.println("Detected: " + obj.getClassName() + " with probability: " + obj.getProbability());
                    Rectangle r = obj.getBoundingBox().getBounds();
                    int x = (int) (r.getX());
                    int y = (int) (r.getY());
                    int w = (int) (r.getWidth());
                    int h = (int) (r.getHeight());
                    Imgproc.rectangle(dst,
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
                    Imgcodecs.imencode(".png", dst, mob); // 이미지 인코딩
                    break;
                }
            }
        } catch (Exception e) {
            throw new ObjectDetectionFailedException();
        } finally {
            cap.release();
        }
        imageBytes = mob.toArray();
        String encodedImage = Base64.getEncoder().encodeToString(imageBytes); // Base64로 인코딩
        log.info(encodedImage);
        return new DetectedObjectResponseDTO(encodedImage);
    }
}
