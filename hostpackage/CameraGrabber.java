package hostpackage;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.opencv.highgui.HighGui;


public class CameraGrabber implements AutoCloseable{
    private VideoCapture capture = null;
    private Mat frame = null;

    public CameraGrabber(){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        capture = new VideoCapture(0);
        if (!capture.isOpened()) {
            System.out.println("Error: Camera not found!");
            return;
        }
        capture.set(Videoio.CAP_PROP_FRAME_WIDTH, 640);
        capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 480);

        frame = new Mat();
    }

    @Override
    public void close() {
        capture.release();
    }

    public Mat getFrame(){
        if(capture.read(frame)){
            return frame;
        }
        else{
            System.out.println("Error: Unable to read frame");
            return null;
        }
    }
}
