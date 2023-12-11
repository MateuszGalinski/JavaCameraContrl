package hostpackage;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.SoftPwm;


public class CameraRotator {
    private final GpioController gpio;
    private final GpioPinDigitalOutput motorOnPin;
    private final int motorPinA;
    private final int motorPinB;

    public CameraRotator(){
        gpio = GpioFactory.getInstance();
        motorPinA = 4;
        motorPinB = 5;
        motorOnPin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_06);
        motorOnPin.high();
        SoftPwm.softPwmCreate(motorPinA, 0, 100);
        SoftPwm.softPwmCreate(motorPinB, 0, 100);
    }

    public CameraRotator(int _motorPinA,int _motorPinB, Pin _motorOnPin){
        gpio = GpioFactory.getInstance();
        motorPinA = _motorPinA;
        motorPinB = _motorPinB;
        motorOnPin = gpio.provisionDigitalOutputPin(_motorOnPin);
        motorOnPin.high();
        SoftPwm.softPwmCreate(motorPinA, 0, 100);
        SoftPwm.softPwmCreate(motorPinB, 0, 100);
    }

    @Override
    protected void finalize(){
        motorOnPin.low();
        gpio.shutdown();
    }

    public void rotateDirectionA() throws InterruptedException{
        SoftPwm.softPwmWrite(motorPinA, 90);

        Thread.sleep(250);

        SoftPwm.softPwmWrite(motorPinA, 0);
    }

    public void rotateDirectionB() throws InterruptedException{
        SoftPwm.softPwmWrite(motorPinB, 90);

        Thread.sleep(250);

        SoftPwm.softPwmWrite(motorPinB, 0);
    }

    public void resetMotors(){
        SoftPwm.softPwmWrite(motorPinA, 0);
        SoftPwm.softPwmWrite(motorPinB, 0);
    }
}