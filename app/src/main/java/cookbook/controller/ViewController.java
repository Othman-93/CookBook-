package cookbook.controller;

import cookbook.view.BackgroundView;
import cookbook.view.Buttons;
import cookbook.view.CafeShopView;
import cookbook.view.ChatBox;
import cookbook.view.MarketView;
import cookbook.view.RestaurantView;
import cookbook.view.Stage2BackButton;
import cookbook.view.StageTwoNavigator;
import cookbook.view.UltimateCookbook;
import cookbook.view.WelcomeView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


/**
 * handle calling classes from view. 
 */
public class ViewController {
    private StackPane root;
    private BackgroundView backgroundView;
    private Stage2BackButton backButton;



    public ViewController(StackPane root) {
        this.root = root;
        this.backgroundView = new BackgroundView();
        this.backButton = new Stage2BackButton(root, this);
    }
      /**
     * Stage One, Login, Registration (userView, admin panel)
     */
      public void stageOneView(Stage primaryStage) {
          root.getChildren().clear();

          // Welcome screen and background setup
          backgroundView.stageOneSetUpBackground(root, primaryStage);
          

          //Title screen
          WelcomeView welcomeView = new WelcomeView();

          root.getChildren().addAll(welcomeView.createWelcomeScreen());
      }

      /**
     * Stage two, Market,Library, Restarant.
     */
      public void StageTwo(String username) {
        //Clears Main Stage Background
        root.getChildren().clear();
        // Welcome screen main Background on Stage1
        backgroundView.stageTwoSetUpBackground(root);

        //Initinalezer Navigator on stage two
        StageTwoNavigator navigator = new StageTwoNavigator();
        root.getChildren().add(navigator.navigator(username));

      }
      /**
     * Stage two, Library
     */
      public void StageTwoLibrary(String username) {
        UltimateCookbook cookbook = new UltimateCookbook(root, username);
        addNavigatorAndBackButton(username, "./assets/background/stageTwo/library/Background.png");
        cookbook.startingButton();
      }
      /**
     * Stage two, Market
     */
      public void StageTwoMarket(String username) {
        MarketView market = new MarketView(root,username);
        addNavigatorAndBackButton(username, "./assets/background/stageTwo/market/marketBackground.png");
        market.startingButton(username);
      }
     /**
     * Stage two, Cafe
     */
      public void StageTwoCoffee(String username) {
        CafeShopView cafeShop = new CafeShopView(root, username);
        addNavigatorAndBackButton(username, "./assets/background/stageTwo/cafeshop/Background.png");
        cafeShop.startingButton();

      }
     /**
     * Stage two, Restaurant
     */
      public void StageTwoRestaurant(String username) {
        RestaurantView restaurant = new RestaurantView(root, username);
        addNavigatorAndBackButton(username, "./assets/background/stageTwo/restaurant/background.png");
        restaurant.startingButton();
      }

      //Clears a scene-graph and adding needed background
      private void setupStageBackground(String backgroundImageUrl) {
        root.getChildren().clear();
        root.getChildren().add(backgroundView.createBackground(backgroundImageUrl));
    }
      //puts backButton in the scene and passing "username"
      private void addNavigatorAndBackButton(String username, String backgroundImageUrl) {
          setupStageBackground(backgroundImageUrl);
          backButton.startingButton(username);
  }
  
}
