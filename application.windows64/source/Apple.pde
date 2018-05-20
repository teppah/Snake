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

  void drawApple() {
    noStroke();
    fill(255, 0, 0);
    ellipseMode(CENTER);
    ellipse(xC, yC, d, d);
  }
  
}
