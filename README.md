# Graphics-Primitive-Generator
A graphics primitive generator built by Java, which is able to visualize execution process of basic graphics algorithms.

### Basic Information:
#### 1.Function:
The function of this program is to demonstrate the algorithm of drawing basic primitives, which is mainly divided into two modules: line drawing primitive generation and filling primitive generation. Among them, the algorithms that can be demonstrated in the line drawing element part are: DDA/Bresenham algorithm for drawing straight lines, midpoint circle generation algorithm, midpoint ellipse generation algorithm; the algorithms that can be demonstrated in the filled element part are: scan conversion filling, recursive region filling, scanning line region filling (non-recursive).

 --------------------------------------------------------------------------
 #### 2.Drawing Area:
Drawing area size is 600x600 pixels, mesh size is 10x10 pixels, a total of 60 rows and 60 columns. In the process of drawing, it is necessary to ensure that the drawn graphics do not exceed the boundary, otherwise the results beyond the part can not be displayed.

 --------------------------------------------------------------------------
 #### 3.Operational instructions
In drawing line drawing primitives, the corresponding parameters can be drawn by clicking the button; in filling primitives, the filling area needs to be determined first, in which the number of vertices is selected by drop-down box and confirmed by clicking the "modify" button; the shape of the area is changed by dragging the vertex by the left key. After that, click the button to scan, transform and fill, or first select the area filling mode, and then right-click inside the area to fill. Speed adjustment needs to input new values, click the button to take effect; color selection can be directly clicked on the button, the current color on the panel also has legend display.

 --------------------------------------------------------------------------
### Samples:
1. Bresenham algorithm
<img src="https://github.com/xqzlgy2/Graphics-Primitive-Generator/blob/master/Pictures/Bresenham.png"  width="250" height="300">
2. DDA algorithm
<img src="https://github.com/xqzlgy2/Graphics-Primitive-Generator/blob/master/Pictures/DDA.png"  width="250" height="300">
3. Mid-point Circle
<img src="https://github.com/xqzlgy2/Graphics-Primitive-Generator/blob/master/Pictures/Midpoint%20Circle.png"  width="250" height="300">
4. Mid-point Ellipse
<img src="https://github.com/xqzlgy2/Graphics-Primitive-Generator/blob/master/Pictures/Midpoint%20Ellipse.png"  width="250" height="300">
5. Scan-conversion Filling
<img src="https://github.com/xqzlgy2/Graphics-Primitive-Generator/blob/master/Pictures/Scan%20Conversion.png"  width="250" height="300">
6. Recursive Filling
<img src="https://github.com/xqzlgy2/Graphics-Primitive-Generator/blob/master/Pictures/Recursive%20Filling.png"  width="250" height="300">
7. Non-recursive Scan Conversion Filling
<img src="https://github.com/xqzlgy2/Graphics-Primitive-Generator/blob/master/Pictures/Non-recursive%20Scan%20Conversion.png"  width="250" height="300">
