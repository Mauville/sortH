package sample;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class Controller {

    private static final Logger LOGGER = LogManager.getLogger(Controller.class);

    @FXML
    Button button = new Button();
    @FXML
    Label P = new Label();
    @FXML
    Label threedee = new Label();
    @FXML
    Label T = new Label();
    @FXML
    Label NP = new Label();
    @FXML
    Label FD = new Label();
    @FXML
    Label EC = new Label();
    @FXML
    Label A = new Label();
    @FXML
    Label O = new Label();
    private final Label SKIP = new Label();
    @FXML
    ImageView imageholder = new ImageView();
    @FXML
    Text logField = new Text();
    @FXML
    GridPane nonum = new GridPane();
    @FXML
    GridPane num = new GridPane();

    //Keyboard shortcuts
    private final KeyCombination PCode = new KeyCodeCombination(KeyCode.DIGIT8);
    private final KeyCombination threedeeCode = new KeyCodeCombination(KeyCode.DIGIT4);
    private final KeyCombination TCode = new KeyCodeCombination(KeyCode.DIGIT2);
    private final KeyCombination NPCode = new KeyCodeCombination(KeyCode.DIGIT6);
    private final KeyCombination FDCode = new KeyCodeCombination(KeyCode.DIGIT8, KeyCombination.CONTROL_DOWN);
    private final KeyCombination ECCode = new KeyCodeCombination(KeyCode.DIGIT4, KeyCombination.CONTROL_DOWN);
    private final KeyCombination ACode = new KeyCodeCombination(KeyCode.DIGIT2, KeyCombination.CONTROL_DOWN);
    private final KeyCombination OCode = new KeyCodeCombination(KeyCode.DIGIT6, KeyCombination.CONTROL_DOWN);
    private final KeyCombination SkipCode = new KeyCodeCombination(KeyCode.DIGIT0, KeyCombination.CONTROL_DOWN);
    private final KeyCombination SkipCode2 = new KeyCodeCombination(KeyCode.DIGIT0);

    private List<File> imageFiles;
    private File cwd;
    private int currentImage = 0;
    private File currentFile;
    private final Runnable rnP = () -> processImage(P);
    private final Runnable rnthreedee = () -> processImage(threedee);
    private final Runnable rnT = () -> processImage(T);
    private final Runnable rnNP = () -> processImage(NP);
    private final Runnable rnFD = () -> processImage(FD);
    private final Runnable rnEC = () -> processImage(EC);
    private final Runnable rnA = () -> processImage(A);
    private final Runnable rnO = () -> processImage(O);
    private final Runnable rnSKIP = () -> processImage(SKIP);

    public void setAccel() {
        Scene scn = FD.getScene();
        scn.getAccelerators().put(PCode, rnP);
        scn.getAccelerators().put(threedeeCode, rnthreedee);
        scn.getAccelerators().put(TCode, rnT);
        scn.getAccelerators().put(NPCode, rnNP);
        scn.getAccelerators().put(FDCode, rnFD);
        scn.getAccelerators().put(ECCode, rnEC);
        scn.getAccelerators().put(ACode, rnA);
        scn.getAccelerators().put(OCode, rnO);
        scn.getAccelerators().put(SkipCode, rnSKIP);
        scn.getAccelerators().put(SkipCode2, rnSKIP);
        LOGGER.info("Set accelerators");

        button.setVisible(false);


    }

    @FXML
    public void initialize() {
        boot();
        SKIP.setText("SKIP");
    }


    private void processImage(Label dir) {
        LOGGER.info(dir.getText());
        if (!(dir.getText().equals("SKIP"))) {
            File directory2Move2 = null;
            try {
                directory2Move2 = new File(cwd.getCanonicalPath() + "\\" + dir.getText());
                LOGGER.info("Moving to : " + directory2Move2);
            } catch (IOException e) {
                LOGGER.error("Failed to convert to Canonical Path");
            }
            try {
                FileUtils.moveFileToDirectory(currentFile, directory2Move2, true);
                LOGGER.info(currentFile + "  " + directory2Move2);
            } catch (IOException e) {
                LOGGER.error("Failed to move file");
                e.printStackTrace();
            }
        }
        logField.setText(dir.getText());
        currentImage++;
        try {
            displayNext();
        } catch (FileNotFoundException e) {
            LOGGER.error("No further images found");
        }
    }

    private void displayNext() throws FileNotFoundException {
        try {
            currentFile = imageFiles.get(currentImage);
            FileInputStream inputstream;
            inputstream = new FileInputStream(currentFile);
            Image image = new Image(inputstream);
            imageholder.setFitHeight(500);
            imageholder.setPreserveRatio(true);
            imageholder.setSmooth(true);
            imageholder.setImage(image);
            inputstream.close();
        } catch (IndexOutOfBoundsException e) {
            LOGGER.warn("End of Images");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void boot() {
        cwd = openFileChooser();
        try {
            imageFiles = ImageHandler.getImages(cwd);
        } catch (IOException e) {
            LOGGER.error("No such directory");
            e.printStackTrace();
        }
        try {
            LOGGER.info("Loading Images");
            imageFiles = ImageHandler.getImages(cwd);
            displayNext();
            LOGGER.info("Loaded Images");
        } catch (IOException e) {
            LOGGER.error("No such directory");
        }
    }

    private File openFileChooser() {
        Stage filer = new Stage();
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose Directory");
        String userWindows = System.getenv("USERPROFILE");
        File defaultDirectory = new File(userWindows);
        chooser.setInitialDirectory(defaultDirectory);
        File mark = chooser.showDialog(filer);
        LOGGER.info("Current path" + mark);
        return mark;
    }

}
