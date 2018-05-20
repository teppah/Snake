class Grid {
  Snake sn;
  int xAmount;
  int yAmount;
  Apple apple;


  Grid(Snake sn) {
    this.sn = sn;
    xAmount = int(width/w);
    yAmount = int(height/w);
    generateApple();
  }

  void show() {
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


  void generateApple() {
    // randomly selects a line from all the possible ones and multiply by w to get its true width
    int appleX = int(random(xAmount)) * w;
    int appleY = int(random(yAmount)) * w;
    //checks if the apple's position overlaps snake parts
    if (sn.contains(appleX, appleY)) {
      generateApple();
    }
    apple =  new Apple(appleX, appleY);
  }
  
  void reset() {
    generateApple();
  }
}
