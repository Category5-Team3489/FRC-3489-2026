package frc.robot.subsystems;

import static edu.wpi.first.units.Units.*;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.ModuleConfig;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.pathfinding.Pathfinding;
import com.pathplanner.lib.util.PathPlannerLogging;
import edu.wpi.first.hal.FRCNetComm.tInstances;
import edu.wpi.first.hal.FRCNetComm.tResourceType;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Twist2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.Alert.AlertType;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.Constants;
import frc.robot.Constants.Mode;
import frc.robot.generated.TunerConstants;
import frc.robot.util.LocalADStarAK;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;
import com.ctre.phoenix6.hardware.TalonFX;
    
public class shooter extends SubsystemBase {
    
    private int motorPort1;
    private int motorPort2;
    private final double distanceFromGround;
    private final double HubHeight = 1.829;
    private TalonFX AngleMotor;
    private TalonFX ShootMotor;
    
    // When called, asks for motor's port and then  creates a motor with the port
    public shooter(int motorPortAngle, int motorPortShoot, double distanceFromG){
        this.motorPort1 = motorPortAngle;
        this.motorPort2 = motorPortShoot;
        this.distanceFromGround = distanceFromG;
        this.AngleMotor = new TalonFX(motorPort1);
        this.ShootMotor = new TalonFX(motorPort2);
    }
    
    public double getNeededAngle(double distance,double initalSpeed, boolean PlusorMinus){
        // True is plus, false is minus.
        if(PlusorMinus == true){
            return Math.atan(
    (Math.pow(initialSpeed, 2) + Math.sqrt(Math.pow(initialSpeed, 4) - 9.8 * (9.8 * Math.pow(distance, 2) + 2 * (HubHeight - distanceFromGround) * Math.pow(initialSpeed, 2)))) 
    / (9.8 * distance)
);
        }else{
            return Math.atan(
    (Math.pow(initialSpeed, 2) - Math.sqrt(Math.pow(initialSpeed, 4) - 9.8 * (9.8 * Math.pow(distance, 2) + 2 * (HubHeight - distanceFromGround) * Math.pow(initialSpeed, 2)))) 
    / (9.8 * distance)
);
        }
    }
}
