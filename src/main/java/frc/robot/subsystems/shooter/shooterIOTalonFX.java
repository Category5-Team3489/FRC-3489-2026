package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.math.MathUtil;

package frc.robot.subsystems.shooter;

public class shooterIOTalonFX implements shooterIO {
    // Create motors
    private final TalonFX angleMotor;
    private final TalonFX shooterMotor;

    @Override
    public void updateInputs(shooterIOInputs inputs) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setShootMotor(double speed) {
        // TODO Auto-generated method stub
        
    }


    @Override
    public void stopMotors() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void shootBall(double speed) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setShootAngle(double angle) {
        // TODO Auto-generated method stub
        
    }

}
