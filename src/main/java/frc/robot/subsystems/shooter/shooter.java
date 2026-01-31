package frc.robot.subsystems.shooter;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class shooter extends SubsystemBase {

  private int motorPort1;
  private int motorPort2;
  private final double distanceFromGround;
  private final double HubHeight = 1.829;
  private TalonFX AngleMotor;
  private TalonFX ShootMotor;

  // When called, asks for motor's port and then  creates a motor with the port
  public shooter(int motorPortAngle, int motorPortShoot, double distanceFromG) {
    this.motorPort1 = motorPortAngle;
    this.motorPort2 = motorPortShoot;
    this.distanceFromGround = distanceFromG;
    this.AngleMotor = new TalonFX(motorPort1);
    this.ShootMotor = new TalonFX(motorPort2);
  }

  // Yo mentor anthony, if you can see this I was wondering
  // If you could check over this function. If its wrong,
  // please tell me why 😭
  public void moveToAngle(double degrees) {
    double rotations = degrees / 360.0;
    PositionDutyCycle request = new PositionDutyCycle(rotations);
    AngleMotor.setControl(request);
  }

  public Command shootAtDistance(double distance) {
    return Commands.run(() -> ShootMotor.setVoltage(distance * 0.3 + 9));
  }

  // Command to move the top motor at a certain speed
  // I am sick rn so I am not writing comments
  public Command moveTopMotor(double speed) {
    return Commands.run(() -> AngleMotor.set(speed));
  }

  public double getNeededAngle(double distance, double initialSpeed, boolean PlusorMinus) {
    // True is plus, false is minus.
    if (PlusorMinus == true) {
      return Math.toDegrees(
          Math.atan(
              (Math.pow(initialSpeed, 2)
                      + Math.sqrt(
                          Math.pow(initialSpeed, 4)
                              - 9.8
                                  * (9.8 * Math.pow(distance, 2)
                                      + 2
                                          * (HubHeight - distanceFromGround)
                                          * Math.pow(initialSpeed, 2))))
                  / (9.8 * distance)));
    } else {
      return Math.toDegrees(
          Math.atan(
              (Math.pow(initialSpeed, 2)
                      - Math.sqrt(
                          Math.pow(initialSpeed, 4)
                              - 9.8
                                  * (9.8 * Math.pow(distance, 2)
                                      + 2
                                          * (HubHeight - distanceFromGround)
                                          * Math.pow(initialSpeed, 2))))
                  / (9.8 * distance)));
    }
  }
}
