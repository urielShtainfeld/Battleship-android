package com.example.ushtinfeld.battleship_uriel;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import components.Cell;
import components.CellListener;
import entities.ComTurn;
import entities.GameController;

import entities.GameRoles;
import entities.Timer;

public class Game extends AppCompatActivity implements CellListener, View.OnClickListener {
    public GameController controller;
    private Button startBtn, randomBtn, clearBtn;
    private Timer timer;
    private android.os.Handler handler, comTurnHandler;
    private int timeCounter = 0;
    private TextView shipsLeft, comShipsLeft, headerShipsLeft, headerComShipsLeft, turn, time;
    private int numOfShipsLeft, numOfcomShipsLeft;
    private ComTurn comTurn;
    private GameRoles gameRole;
    private TextView[] shipsToPlain;
    String level;
    LinearLayout ColsLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.level = this.getIntent().getStringExtra("Level");
        controller = new GameController(level);
        controller.createSetShipsBoard(this);
        controller.initVars(this);
        controller.setRandomMode(false);
        setButtons();
    }

    private void setButtons() {
        startBtn = (Button) findViewById(R.id.Start);
        startBtn.setOnClickListener(this);
        startBtn.setClickable(false);

        randomBtn = (Button) findViewById(R.id.Random);
        randomBtn.setOnClickListener(this);

        clearBtn = (Button) findViewById(R.id.Clear);
        clearBtn.setOnClickListener(this);

        shipsLeft = findViewById(R.id.shipsLeft);
        shipsLeft.setVisibility(View.GONE);
        comShipsLeft = findViewById(R.id.COMShipsLeft);
        comShipsLeft.setVisibility(View.GONE);
        headerShipsLeft = findViewById(R.id.shipLeftHeader);
        headerShipsLeft.setVisibility(View.GONE);
        headerComShipsLeft = findViewById(R.id.COMShipLeftHeader);

        headerComShipsLeft.setText("Ships left to plain");
        turn = findViewById(R.id.turn);
        turn.setVisibility(View.GONE);
        shipsToPlain = new TextView[controller.getShipsSizes().length];
        int[] shipSizes = controller.getShipsSizes().clone();
        LinearLayout ColsLayout = new LinearLayout(this);
        ColsLayout.setBackgroundColor(Color.TRANSPARENT);
        ColsLayout.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 0; i < shipSizes.length; i++) {
            shipsToPlain[i] = new TextView(this);
            shipsToPlain[i].setText(Integer.toString(shipSizes[i]) + ",");
            shipsToPlain[i].setTextSize(30);
            ColsLayout.addView(shipsToPlain[i]);
        }
        ColsLayout.setGravity(Gravity.CENTER);
        TableLayout viewsTable = (TableLayout) this.findViewById(R.id.topScoresTable);
        viewsTable.setGravity(Gravity.CENTER);
        viewsTable.addView(ColsLayout);


    }

    public void buttonClicked(Cell cell) {
        if (controller.isSetMode()) {
            int col = cell.getCol();
            int row = cell.getRow();
            controller.setShips(col, row, this);
        } else {
            int col = cell.getCol();
            int row = cell.getRow();
            if (controller.handelHit(col, row, this)) {
                computerTurn();
            }

        }
    }

    private void computerTurn() {
        turn.setText(R.string.comTurn);
        turn.setTextColor(Color.RED);
        setAllButtons(false);
        comTurnHandler.postDelayed(comTurn, (long) (Math.random() * 4000 + 1500));
    }

    public void setAllButtons(boolean b) {
        for (Cell cellArray[] : controller.getSetBoard()) {
            for (Cell cell : cellArray) {
                cell.setClickable(b);
            }

        }
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Clear:
                controller.clearBoard(this);
                startBtn.setClickable(false);
                setButtons();
                break;
            case R.id.Random:
                controller.clearBoard(this);
                controller.setRandomMode(true);
                controller.setShipsRandom(this);
                controller.setRandomMode(false);
                startBtn.setClickable(true);
                break;
            case R.id.Start:
                setButtonsForStart();
                controller.setSetMode(false);
                controller.createUserBoard(this);
                controller.clearBoard(this);
                controller.setRandomMode(true);
                controller.setCompBoard(true);
                controller.setShipsRandom(this);
                controller.setRandomMode(false);
                controller.setCompBoard(false);
                controller.setTextViews(this);
                comTurnHandler = new Handler();
                comTurn = new ComTurn(comTurnHandler, this);
                gameRole = new GameRoles(controller.getPlacedShips(), controller.getPlacedShips());
                this.setNumOfcomShipsLeft(controller.getPlacedShips());
                this.setNumOfShipsLeft(controller.getPlacedShips());
                timerRun();
                break;

        }
    }

    private void setButtonsForStart() {
        startBtn.setVisibility(View.GONE);
        randomBtn.setVisibility(View.GONE);
        clearBtn.setVisibility(View.GONE);

        shipsLeft.setVisibility(View.VISIBLE);
        comShipsLeft.setVisibility(View.VISIBLE);
        headerShipsLeft.setVisibility(View.VISIBLE);
        turn.setVisibility(View.VISIBLE);
        headerComShipsLeft.setText(R.string.com_ships_left);
    }

    private void timerRun() {
        // Timer running
        handler = new Handler() {
            public void handleMessage(Message message) {
                time = (TextView) findViewById(R.id.chronometer);
                time.setText("Time: " + (timeCounter / 60 < 10 ? "0" : "") + timeCounter / 60 + ":" + (timeCounter % 60 < 10 ? "0" : "") + timeCounter % 60);
                timeCounter++;
            }
        };
        timer = new Timer(handler);
        timer.start();
    }

    public TextView getShipsLeft() {
        return shipsLeft;
    }

    public TextView getComShipsLeft() {
        return comShipsLeft;
    }

    public TextView getTurn() {
        return turn;
    }

    public void setShipsLeft(TextView shipsLeft) {
        this.shipsLeft = shipsLeft;
    }

    public void setComShipsLeft(TextView comShipsLeft) {
        this.comShipsLeft = comShipsLeft;
    }

    public void setTurn(TextView turn) {
        this.turn = turn;
    }

    public GameRoles getGameRole() {
        return gameRole;
    }

    public TextView getTime() {
        return time;
    }

    public int getNumOfShipsLeft() {
        return numOfShipsLeft;
    }

    public void setNumOfShipsLeft(int numOfShipsLeft) {
        this.numOfShipsLeft = numOfShipsLeft;
    }

    public void minusNumOfShipsLeft() {
        this.numOfShipsLeft--;
    }

    public int getNumOfcomShipsLeft() {
        return numOfcomShipsLeft;
    }

    public void setNumOfcomShipsLeft(int numOfcomShipsLeft) {
        this.numOfcomShipsLeft = numOfcomShipsLeft;
    }

    public void minusNumOfcomShipsLeft() {
        this.numOfcomShipsLeft--;
    }

    public String getLevel() {
        return level;
    }

    public TextView[] getShipsToPlain() {
        return shipsToPlain;
    }

    public Button getStartBtn() {
        return startBtn;
    }
}
