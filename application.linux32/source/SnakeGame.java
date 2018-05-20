import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class SnakeGame extends PApplet {

final int NORTH = 0;
final int EAST = 1;
final int SOUTH = 2;
final int WEST = 3;
Snake sn;
Grid grid;
int w; // snakeWidth
int state;
//Game state:
//0 - start screen;
//1 - game screen;
//2 - death screen;

PFont f1;
PFont f2;
PFont f3;

public void setup() {
  
  background(255);
  noStroke();
  w = 20;
  sn = new Snake(color(141, 34, 237));
  grid = new Grid(sn);
  state = 0;
  f1 = createFont("ARCADECLASSIC.TTF", 60);
  f2 = createFont("Courier New", 40);
  f3 = createFont("Courier New Bold", 40);
  startMenu();
}

public void draw() {
  switch(state) {
  case 0:
    startMenu();
    break;
  case 1:
    game();
    break;
  case 2:
    death();
  }
}

public void startMenu() {
  if (frameCount % 30 == 0) {
    background(41, 211, 240);
    noStroke();
    textAlign(CENTER);
    textFont(f1);
    textSize(60);
    fill(255, 0, 0);
    text("Classic  Snake  Game", width/2, height/3);
    fill(0);
    textFont(f2);
    textSize(30);
    text("Use \"WASD\" to move.", width/2, height/2);
    textFont(f3);
    fill(PApplet.parseInt(random(220)), PApplet.parseInt(random(220)), PApplet.parseInt(random(220)));
    textSize(30);
    text("Press any key to start.", width/2, 2*height/3);
  }
  if (keyPressed) {
    state = 1;
  }
}

public void game() { 
  if (frameCount % 5 == 0) {
    background(255);
    grid.show();
    checkEat();
    sn.display();
    checkCollide();
    checkInside();
  }
}

public void death() {
  background(0);
  fill(255);
  textFont(f2);
  textSize(60);
  textMode(CENTER);
  text("You Died!", width/2, 2 * height/5);
  String score = "Your score: " + (sn.s - 1) ;
  textSize(40);
  text(score, width/2, height/2); 
  textSize(30);
  text("Press your mouse to restart.", width/2, 2 * height / 3);
  if (mousePressed) {
    grid.reset();
    sn.reset();
    state = 0;
    mousePressed = false;
    keyPressed = false;
  }
}

public void checkEat() {
  // check if snake ate the apple
  if (sn.contains(grid.apple.x, grid.apple.y)) {
    sn.grow();
    grid.generateApple();
  }
}

public void checkCollide() {
  int x = sn.x.get(sn.x.size() - 1);
  int y = sn.y.get(sn.y.size() - 1);
  for (int i = 0; i < sn.x.size() - 1; i++) {
    int exisX = sn.x.get(i);
    int exisY = sn.y.get(i);
    if (exisX == x && exisY == y && sn.s != 1) {
      state = 2;
    }
  }
}

public void checkInside() {
  int x = sn.x.get(sn.x.size() - 1);
  int y = sn.y.get(sn.y.size() - 1);
  if (x < 0 || x > width || y < 0 || y > height) {
    state = 2;
  }
}

public void keyPressed() {
  int newDir = 0;
  if (keyPressed) {
    sn.inMotion = true;
    switch(key) {
    case 'w':
      newDir = NORTH;
      break;
    case 'd':
      newDir = EAST;
      break;
    case 's':
      newDir = SOUTH;      
      break;
    case 'a':
      newDir = WEST;
      break;
    }
  }
  sn.dir = sn.s == 1 ? newDir : (sn.dir != opposite(newDir) ? newDir : sn.dir);
  // if snake size is 1, then allow it to change to any direction
  // else, if the newDir is not opposite to currDir, then allow it to change, else set the value to the same
}

public int opposite(int dir) {
  // returns the opposite direction from the argument dir;
  switch(dir) {
  case NORTH: 
    return SOUTH;
  case EAST: 
    return WEST;
  case SOUTH: 
    return NORTH;
  case WEST: 
    return EAST;
  }
  return -1; //should not ever happen
}
class Apple {
  int xC;
  int yC;
  int d = 10;
  int x;
  int y;
  
  Apple(int x, int y) {
    this.x = x;
    this.y = y;
    
    // used for displaying the apple using CENTER mode
    this.xC = x + w / 2;
    this.yC = y + w / 2;
  }

  public void drawApple() {
    noStroke();
    fill(255, 0, 0);
    ellipseMode(CENTER);
    ellipse(xC, yC, d, d);
  }
  
}
class Color{
  int r, g, b;
  
  Color(int r, int g, int b) {
    this.r = r;
    this.g = g;
    this.b = b;
  }
}
class Grid {
  Snake sn;
  int xAmount;
  int yAmount;
  Apple apple;


  Grid(Snake sn) {
    this.sn = sn;
    xAmount = PApplet.parseInt(width/w);
    yAmount = PApplet.parseInt(height/w);
    generateApple();
  }

  public void show() {
    strokeWeight(1);
    stroke(34, 194, 237);
    // drawing all the vertical lines
    for (int i = 0; i < xAmount; i++) {
      int xPos = i * w;
      line(xPos, 0, xPos, height);
    }
    // drawing all the horizontal lines
    for (int i = 0; i < yAmount; i++) {
      int yPos = i * w;
      line(0, yPos, width, yPos);
    }
    apple.drawApple();
  }


  public void generateApple() {
    // randomly selects a line from all the possible ones and multiply by w to get its true width
    int appleX = PApplet.parseInt(random(xAmount)) * w;
    int appleY = PApplet.parseInt(random(yAmount)) * w;
    //checks if the apple's position overlaps snake parts
    if (sn.contains(appleX, appleY)) {
      generateApple();
    }
    apple =  new Apple(appleX, appleY);
  }
  
  public void reset() {
    generateApple();
  }
}
class Snake {
  ArrayList<Integer> x;
  ArrayList<Integer> y;
  int dir;
  //Directions: 
  //0 - North
  //1 - East
  //2 - South
  //3 - West
  int c;
  int s; // snake size
  boolean inMotion;

  // stores the color data
  ArrayList<Color> colors;

  Snake(int c) {
    this.c = c;
    this.dir = 1;
    s = 1;
    x = new ArrayList();
    y = new ArrayList();
    x.add(width/2);
    y.add(height/2);
    inMotion = false;
    colors = new ArrayList();
    colors.add(new Color(PApplet.parseInt(random(220)), PApplet.parseInt(random(220)), PApplet.parseInt(random(220))));
  }

  public void display() {
    fill(c);
    noStroke();
    for (int i = 0; i < x.size(); i++) {
      Color cC = colors.get(i);
      fill(cC.r, cC.g, cC.b);
      rect(x.get(i), y.get(i), w, w);
    }
    if (inMotion) {
      //if snake is going either north or south, add new x of value the previous entry in the arraylist, since its not moving horizontally
      //else if its going east: take previous x + width, west: previous x - width
      x.add((dir == NORTH || dir == SOUTH) ? x.get(x.size() - 1) : (dir == EAST ? x.get(x.size() - 1) + w : x.get(x.size() - 1) - w));

      //if snake is going either east or west, add new y of value the previous entry in the arraylist, since its not moving vertically
      //else if its going south: take previous x + width, north: previous x - width
      y.add((dir == EAST || dir == WEST) ? y.get(y.size() - 1) : (dir == SOUTH ? y.get(y.size() - 1) + w : y.get(y.size() - 1) - w));
      x.remove(0);
      y.remove(0);
      colors.add(new Color(PApplet.parseInt(random(220)), PApplet.parseInt(random(220)), PApplet.parseInt(random(220))));
      colors.remove(0);
    }
  }

  // check if snake contains those coordinates
  public boolean contains(int x, int y) {
    for (int i = 0; i < this.x.size(); i++) {
      if (this.x.get(i) == x && this.y.get(i) == y) {
        return true;
      }
    }
    return false;
  }

  //add a new snake part
  public void grow() {
    s++;

    //make the snake grow in the appropriate direction
    x.add(x.size(), (dir == NORTH || dir == SOUTH) ? x.get(x.size() - 1) : (dir == EAST ? x.get(x.size() - 1) + w : x.get(x.size() - 1) - w));
    y.add(y.size(), (dir == EAST || dir == WEST) ? y.get(y.size() - 1) : (dir == SOUTH ? y.get(y.size() - 1) + w : y.get(y.size() - 1) - w));
    colors.add(new Color(PApplet.parseInt(random(220)), PApplet.parseInt(random(220)), PApplet.parseInt(random(220))));
  }

  public void reset() {
    dir = 0;
    x.clear();
    y.clear();
    s = 1;
    inMotion = false;
    x.add(width/2);
    y.add(height/2);
  }
}
  public void settings() {  size(600, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "SnakeGame" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
