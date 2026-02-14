# FRC 3489

## [Vision](/src/main/java/frc/robot/subsystems/vision/Vision.java)
- Number of cameras: 2
    - Front Left (ThriftyCam)
    - Front Right (ThriftyCam)
- Coprocessor:
    - OrangePi 5


## [Intake](/src/main/java/frc/robot/subsystems/intake/intake.java)
- Number of motor(s): 2
    - Bottom roller ([KrakenX44](/src/main/java/frc/robot/subsystems/intake/intakeIOTalonFX.java))
    - Actuator ([KrakenX44](/src/main/java/frc/robot/subsystems/intake/intakeIOTalonFX.java))


## [Indexer](/src/main/java/frc/robot/subsystems/indexer/index.java)
- Number of motor(s): 2
    - Agitator ([KrakenX44](/src/main/java/frc/robot/subsystems/indexer/indexerIOTalonFX.java))
    - Feeder ([KrakenX44](/src/main/java/frc/robot/subsystems/indexer/indexerIOTalonFX.java))


## [Climber](/src/main/java/frc/robot/subsystems/climber/climber.java)
- Number of motor(s): 2
    - Lift ([KrakenX44](/src/main/java/frc/robot/subsystems/climber/climberIOTalonFX.java))
    - Follower ([KrakenX44](/src/main/java/frc/robot/subsystems/climber/climberIOTalonFX.java))


## [Shooter](/src/main/java/frc/robot/subsystems/shooter/shooter.java)
- Number of motor(s): 3
    - Fly wheel ([KrakenX60](/src/main/java/frc/robot/subsystems/shooter/shooterIOTalonFX.java))
    - Follow ([KrakenX60](/src/main/java/frc/robot/subsystems/shooter/shooterIOTalonFX.java))
    - Adjust hood ([KrakenX44](/src/main/java/frc/robot/subsystems/shooter/shooterIOTalonFX.java))


## [Turrent](/src/main/java/frc/robot/subsystems/turrent/turrent.java)
- Number of motor(s): 1
    - Rotate shooter ([KrakenX44](/src/main/java/frc/robot/subsystems/turret/turretIOTalonFX.java))
