
package frc.robot.subsystems.shooter;

import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;

public class shooterIOTalonFX implements shooterIO {
    // Create motors
    private final TalonFX angleMotor;
    private final TalonFX shooterMotor;

    public shooterIOTalonFX(int shooterMotorPort, int angleMotorPort){
        angleMotor = new TalonFX(angleMotorPort);
        shooterMotor = new TalonFX(shooterMotorPort);
    }
    
    @Override
    public void updateInputs(shooterIOInputs inputs) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void stopMotors() {
        // TODO Auto-generated method stub
        shooterMotor.set(0);
        angleMotor.set(0);
    }  

    @Override
    public void shootBall(double speed) {
        // Check this code fs
        shooterMotor.set(speed);
    }

    @Override
    public void setShootAngle(double degrees) {
        // Check this code fs
        double rotations = degrees / 360.0;
        PositionDutyCycle request = new PositionDutyCycle(rotations);
        angleMotor.setControl(request);
    }

}
