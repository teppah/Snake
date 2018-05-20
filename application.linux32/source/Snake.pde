class Snake {
  ArrayList<Integer> x;
  ArrayList<Integer> y;
  int dir;
  //Directions: 
  //0 - North
  //1 - East
  //2 - South
  //3 - West
  color c;
  int s; // snake size
  boolean inMotion;

  // stores the color data
  ArrayList<Color> colors;

  Snake(color c) {
    this.c = c;
    this.dir = 1;
    s = 1;
    x = new ArrayList();
    y = new ArrayList();
    x.add(width/2);
    y.add(height/2);
    inMotion = false;
    colors = new ArrayList();
    colors.add(new Color(int(random(220)), int(random(220)), int(random(220))));
  }

  void display() {
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
      colors.add(new Color(int(random(220)), int(random(220)), int(random(220))));
      colors.remove(0);
    }
  }

  // check if snake contains those coordinates
  boolean contains(int x, int y) {
    for (int i = 0; i < this.x.size(); i++) {
      if (this.x.get(i) == x && this.y.get(i) == y) {
        return true;
      }
    }
    return false;
  }

  //add a new snake part
  void grow() {
    s++;

    //make the snake grow in the appropriate direction
    x.add(x.size(), (dir == NORTH || dir == SOUTH) ? x.get(x.size() - 1) : (dir == EAST ? x.get(x.size() - 1) + w : x.get(x.size() - 1) - w));
    y.add(y.size(), (dir == EAST || dir == WEST) ? y.get(y.size() - 1) : (dir == SOUTH ? y.get(y.size() - 1) + w : y.get(y.size() - 1) - w));
    colors.add(new Color(int(random(220)), int(random(220)), int(random(220))));
  }

  void reset() {
    dir = 0;
    x.clear();
    y.clear();
    s = 1;
    inMotion = false;
    x.add(width/2);
    y.add(height/2);
  }
}
