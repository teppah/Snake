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

void setup() {
  size(600, 600);
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

void draw() {
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

void startMenu() {
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
    fill(int(random(220)), int(random(220)), int(random(220)));
    textSize(30);
    text("Press any key to start.", width/2, 2*height/3);
  }
  if (keyPressed) {
    state = 1;
  }
}

void game() { 
  if (frameCount % 5 == 0) {
    background(255);
    grid.show();
    checkEat();
    sn.display();
    checkCollide();
    checkInside();
  }
}

void death() {
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

void checkEat() {
  // check if snake ate the apple
  if (sn.contains(grid.apple.x, grid.apple.y)) {
    sn.grow();
    grid.generateApple();
  }
}

void checkCollide() {
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

void checkInside() {
  int x = sn.x.get(sn.x.size() - 1);
  int y = sn.y.get(sn.y.size() - 1);
  if (x < 0 || x > width || y < 0 || y > height) {
    state = 2;
  }
}

void keyPressed() {
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

int opposite(int dir) {
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
