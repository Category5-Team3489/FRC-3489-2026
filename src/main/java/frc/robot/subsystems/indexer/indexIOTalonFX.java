package frc.robot.subsystems.indexer;

public class indexIOTalonFX implements indexIO {
    private final indexIOInputs inputs = new indexIOInputs();
    private final com.ctre.phoenix6.hardware.TalonFX indexMotor;

    public indexIOTalonFX() {
        // Initialize hardware here (e.g., motor controllers)
        indexMotor = new com.ctre.phoenix6.hardware.TalonFX(inputs.motorPort);
    }

    @Override
    public void turnMotor(double speed) {
        indexMotor.set(speed);
    }
}