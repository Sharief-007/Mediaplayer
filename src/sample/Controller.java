package sample;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML private MenuItem open_file,open_dir,preview;
    @FXML private Slider slider,volume;
    @FXML private Button play,open,cancel;
    @FXML private Label position;
    @FXML private Label time;
    @FXML private Label sound;
    @FXML private MediaView view;
    @FXML private BorderPane layout;
    @FXML private HBox hbox2,hbox1;
    @FXML private StackPane stackpane;
    @FXML private VBox vbox,vbox2,spectrumbox;
    @FXML private MenuItem full_screen;
    @FXML private ListView listview;


    boolean isListOpen = false;
    boolean playing =false;
    File file,folder,files[];
    FileChooser chooser = new FileChooser();
    DirectoryChooser directoryChooser = new DirectoryChooser();
    Media media;
    MediaPlayer player;
    Timeline slidein = new Timeline(),slideout = new Timeline(),fadein = new Timeline(),
            fadeout = new Timeline(),shadein = new Timeline(),shadeout = new Timeline();
    FileFilter folderFilter = new FileFilter() {
        public boolean accept(File file) {
            if (file.getName().endsWith(".mp4") ||file.getName().endsWith(".flv")) {
                return true;
            }
            return false;
        }
    };


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        vbox2.setPrefWidth(200);
        setFadeAnimations();
        fadein.play();
        volume.setMin(0);
        volume.setMax(1);
        volume.setValue(0.7);
    }

    @FXML private void openFile(ActionEvent event)
    {
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("select a file (*.mp4)","*.mp4");
        chooser.getExtensionFilters().add(filter);
        file = chooser.showOpenDialog((Stage)stackpane.getScene().getWindow());
        if(playing && file!= null)
        {
            player.stop();
        }
        if (file!=null)
        {
            Play(file);
        }
    }

    @FXML private void openDir(ActionEvent event)
    {
        folder = directoryChooser.showDialog(null);

        if (folder != null)
        {
            files = new File[folder.listFiles(folderFilter).length];
            files = folder.listFiles(folderFilter);
            listview.getItems().clear();
            for(File f:files)
            {
                listview.getItems().add(f.getName());
            }
            fadeout.play();
        }
    }

    @FXML private void playSelected(ActionEvent event)
    {
        fadein.play();
        int i=listview.getSelectionModel().getSelectedIndex();
        if(playing)
        {
            player.stop();
        }
        Play(files[i]);
    }
    @FXML private void Cancel()
    {
        fadein.play();
    }

    @FXML private void previewHandler()
    {
        if(isListOpen)
        {
            fadein.play();
        }
        else {
            fadeout.play();
        }
    }

    private void setSliderRange(Slider slider,double min,double max)
    {
        slider.setMin(min);
        slider.setMax(max);
    }

    @FXML private void playAction(ActionEvent event)
    {
        if (playing)
        {
            player.pause();
            play.setText("play");
            playing = false;
        }
        else
        {
            playing=true;
            player.play();
            play.setText("pause");
        }
    }

    String FormatMinutes(double seconds)
    {
        int mins = (int)seconds/60;
        return String.valueOf(mins);
    }
    String FormatSeconds(double seconds)
    {
        int sec = (int)seconds%60;
        return String.valueOf(sec);
    }



    @FXML private void open_full_Screen()
    {
        Stage stage = (Stage) stackpane.getScene().getWindow();
        stage.setFullScreen(true);
        if (playing)
        {
            view.setFitWidth(stage.getWidth());
            view.setFitHeight(stage.getHeight());
        }
        //shadeout.play();
    }
    private void setDimensions(Media media,MediaPlayer player,MediaView view)
    {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println(screen);


        int width = media.getWidth();
        int height = media.getHeight();
        System.out.println(width+":width,"+height+":height");

        Stage stage = (Stage) view.getParent().getScene().getWindow();
        stage.setWidth(width);
        stage.setHeight(height);
        view.setFitHeight(height);
        view.setFitWidth(width);
    }
    @FXML private void Exit()
    {
        System.exit(0);
    }

    private void setFadeAnimations()
    {
        fadein.getKeyFrames().addAll(
                new KeyFrame(new Duration(0),
                        new KeyValue(vbox2.translateXProperty(),0)),
                new KeyFrame(new Duration(300),
                        new KeyValue(vbox2.translateXProperty(),200)
                ));
        fadeout.getKeyFrames().addAll(
                new KeyFrame(new Duration(0),
                        new KeyValue(vbox2.translateXProperty(),200)),
                new KeyFrame(new Duration(300),
                        new KeyValue(vbox2.translateXProperty(),0))
        );
//        shadein.getKeyFrames().addAll(
//                new KeyFrame(new Duration(0),
//                        new KeyValue(spectrumbox.translateYProperty(),0)),
//                new KeyFrame(new Duration(300),
//                        new KeyValue(spectrumbox.translateYProperty(),125)
//                ));
//        shadeout.getKeyFrames().addAll(
//                new KeyFrame(new Duration(0),
//                        new KeyValue(spectrumbox.translateYProperty(),125)),
//                new KeyFrame(new Duration(300),
//                        new KeyValue(spectrumbox.translateYProperty(),0))
//        );

        System.out.println("THis block executed");
    }



    @FXML private void Play(File file)
    {
        try
        {
            media = new Media(file.toURI().toString());
            player = new MediaPlayer(media);
            view.setMediaPlayer(player);

            hbox1.getChildren().clear();
            Rectangle r[] = new Rectangle[player.getAudioSpectrumNumBands()];
            for (int i=0;i<r.length;i++)
            {
                r[i] = new Rectangle();
                r[i].setWidth((9));
                r[i].setHeight(4);
                r[i].setFill(Color.YELLOWGREEN);
                hbox1.getChildren().add(r[i]);
            }

            player.play();
            player.setVolume(0.7);
            full_screen.setDisable(false);
            play.setDisable(false);
            play.setText("pause");
            playing = true;
            setSliderRange(slider,0,player.getTotalDuration().toSeconds());
            System.out.println(slider.getMax()+","+slider.getMin());

            player.setAudioSpectrumListener(new AudioSpectrumListener() {
                @Override
                public void spectrumDataUpdate(double v, double v1, float[] floats, float[] floats1) {
                    try
                    {
                        for (int i =0;i<r.length;i++)
                        {
                            double h = floats[i]+60;
                            if (h>2)
                            {
                                r[i].setHeight(h);
                            }
                        }
                    }
                    catch (NullPointerException n)
                    {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setContentText(n.getMessage());
                        a.showAndWait();
                    }
                }
            });


            stackpane.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    slideout.play();
                }
            });
            stackpane.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    slidein.play();
                }
            });

            player.setOnReady(new Runnable() {
                @Override
                public void run() {

                    setDimensions(media,player,view);


                    double total = player.getTotalDuration().toSeconds();
                    setSliderRange(slider,0,total);
                    time.setText(FormatMinutes(total)+" : "+FormatSeconds(total));
                    System.out.println(slider.getMax()+","+slider.getMin());

                    slidein.getKeyFrames().addAll(
                            new KeyFrame(new Duration(0),
                                    new KeyValue(vbox.translateYProperty(),0)),
                            new KeyFrame(new Duration(300),
                                    new KeyValue(vbox.translateYProperty(),50))
                    );
                    slideout.getKeyFrames().addAll(
                            new KeyFrame(new Duration(0),
                                    new KeyValue(vbox.translateYProperty(),50)),
                            new KeyFrame(new Duration(300),
                                    new KeyValue(vbox.translateYProperty(),0))
                    );
                }
            });
            player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration t1) {
                    double pos = t1.toSeconds();
                    slider.setValue(pos);
                    position.setText(FormatMinutes(pos)+" : "+FormatSeconds(pos));
                }
            });
            slider.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    player.seek(Duration.seconds(slider.getValue()));
                }
            });
            slider.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    player.seek(Duration.seconds(slider.getValue()));
                }
            });
            volume.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    if (volume.isPressed())
                    {
                        player.setVolume(volume.getValue());
                    }
                }
            });
        }
        catch (Exception ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }
}