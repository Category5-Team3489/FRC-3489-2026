package frc.robot.sim;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;

public final class SimChessLauncher {
  private static final String COMMAND_KEY = "Sim/VoiceCommand";
  private static final String STATUS_KEY = "Sim/VoiceStatus";
  private static final String HELP_KEY = "Sim/VoiceHelp";

  private static String lastCommand = "";
  private static boolean initialized = false;

  private SimChessLauncher() {}

  public static void init() {
    if (initialized) {
      return;
    }

    SmartDashboard.putString(COMMAND_KEY, "");
    SmartDashboard.putString(STATUS_KEY, "Type 'play chess' in Sim/VoiceCommand");
    SmartDashboard.putString(
        HELP_KEY, "Phrase trigger supported in SIM: 'play chess' or 'i want to play chess'");
    initialized = true;
  }

  public static void periodic() {
    if (!initialized) {
      init();
    }

    String command = SmartDashboard.getString(COMMAND_KEY, "").trim();
    String normalized = command.toLowerCase(Locale.ROOT);
    if (normalized.isEmpty() || normalized.equals(lastCommand)) {
      return;
    }
    lastCommand = normalized;

    if (normalized.contains("play chess")) {
      launchChess();
      SmartDashboard.putString(COMMAND_KEY, "");
      return;
    }

    SmartDashboard.putString(STATUS_KEY, "Unknown phrase: '" + command + "'");
  }

  private static void launchChess() {
    Path chessFile = Path.of("src", "main", "deploy", "sim-chess.html").toAbsolutePath();
    if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
      SmartDashboard.putString(STATUS_KEY, "Desktop browser open not supported on this system");
      return;
    }

    try {
      Desktop.getDesktop().browse(chessFile.toUri());
      SmartDashboard.putString(STATUS_KEY, "Opened local two-player chess game");
    } catch (IOException ex) {
      SmartDashboard.putString(STATUS_KEY, "Failed to open chess game: " + ex.getMessage());
      DriverStation.reportError("Failed to open sim chess page", ex.getStackTrace());
    }
  }
}
