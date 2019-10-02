import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.Set;
import java.util.HashSet;
import java.util.Comparator;
import java.util.Iterator;
import javax.swing.*;

public class GraphicsGenerator extends JFrame implements ActionListener, MouseListener
{
	//以下list存放计算结果
	List<Point> DDAline = new ArrayList<Point>();
	List<Point> Bresenhamline = new ArrayList<Point>();
	List<Point> Bresenhamcircle = new ArrayList<Point>();
	List<Point> Bresenhamoval = new ArrayList<Point>();
	static List<Point> Vertexs = new ArrayList<Point>();
	static List<Point> Edges = new ArrayList<Point>();
	List<Point> FillPoints = new ArrayList<Point>();
	
	//线画图元控件声明
	GPanel g;
	JPanel pn, colorPanel; //后者用于表示当前选择的颜色
	JButton ddab, bresenhamb, exitb, circleb, ovalb, colorb, speedb, modeb;
	JLabel startl, endl, centerl, radiusl, radiusal, radiusbl;
	JTextField x1t, y1t, x2t, y2t, xt, yt, radiust, radiusat, radiusbt, speedt;
	
	//填充图元控件声明
	JPanel pn2, colorPanel2;
	JButton exitb2, colorb2, speedb2, modeb2, scanb, numberb;
	JTextField speedt2;
	JLabel boundaryl1, boundaryl2, numberl, seedl;
	JRadioButton boundaryr1, boundaryr2;
	JComboBox<String> numbert;
	
	//计时器
	private Timer timer;
	int delay = 100;
	List<Point> workpoints = new ArrayList<Point>(); //需要用动画绘制的点集
	List<Point> paintpoints = new ArrayList<Point>(); //动画中每次绘制的点
	
	//其他变量
	Color color = Color.blue; //绘制的颜色，默认为蓝色
	int number = 6; //多边形顶点数，默认为6个
	boolean clicked = false; //判断是否按下顶点
	int vertexindex; //按下的顶点编号
	static boolean workmode = false; //工作模式，false表示线画图元
	List<List<ET>> edgeTable = new ArrayList<List<ET>>(); //边表
	List<AET> activeEdgeTable = new ArrayList<AET>(); //活化边表
	
	private void init_panel()
	{
		//定义控件和布局(线画图元)
		pn = new JPanel(new GridLayout(5,5));
		colorPanel = new JPanel();
		colorPanel.setBackground(color);
		startl = new JLabel("起始点");
		x1t = new JTextField(2);
		y1t = new JTextField(2);
		endl = new JLabel("结束点");
		x2t = new JTextField(2);
		y2t = new JTextField(2);
		ddab = new JButton("DDA");
		bresenhamb = new JButton("Bresenham");
		exitb = new JButton("退出");
		centerl = new JLabel("圆心");
		xt = new JTextField(2);
		yt = new JTextField(2);
		radiusl = new JLabel("半径");
		radiust = new JTextField(2);
		circleb = new JButton("中点圆");
		radiusal = new JLabel("长轴");
		radiusat = new JTextField(2);
		radiusbl = new JLabel("短轴");
		radiusbt = new JTextField(2);
		ovalb = new JButton("椭圆");
		colorb = new JButton("选择颜色");
		speedt = new JTextField(4);
		speedt.setText(delay+"");
		speedb = new JButton("速度调节:ms");
		modeb = new JButton("切换模式");
				
		//添加监听器(线画图元)
		ddab.addActionListener(this);
		bresenhamb.addActionListener(this);
		exitb.addActionListener(this);
		circleb.addActionListener(this);
		ovalb.addActionListener(this);
		colorb.addActionListener(this);
		speedb.addActionListener(this);
		modeb.addActionListener(this);
				
		//设置按钮颜色(线画图元)
		ddab.setForeground(Color.blue);
		bresenhamb.setForeground(Color.blue);
		exitb.setForeground(Color.red);
		circleb.setForeground(Color.blue);
		ovalb.setForeground(Color.blue);
		colorb.setForeground(color);
		speedb.setForeground(Color.red);
		modeb.setForeground(Color.red);
				
		//将控件添加到面板(线画图元)
		pn.add(startl); pn.add(x1t); pn.add(y1t);
		pn.add(ddab); pn.add(bresenhamb);
		pn.add(endl); pn.add(x2t); pn.add(y2t);
		pn.add(circleb); pn.add(ovalb);
		pn.add(centerl); pn.add(xt); pn.add(yt);
		pn.add(radiusl); pn.add(radiust);
		pn.add(radiusal); pn.add(radiusat);
		pn.add(radiusbl); pn.add(radiusbt);
		pn.add(modeb);
		pn.add(colorPanel); pn.add(colorb);
		pn.add(speedt); pn.add(speedb);
		pn.add(exitb);
	}
	
	private void init_panel2()
	{
		//定义控件和布局(填充图元)
		numberl = new JLabel("顶点数：");
		String numbers[] = {"6", "5", "4", "3"};
		numbert= new JComboBox<String>(numbers);
		numberb = new JButton("修改");
		colorPanel2 = new JPanel();
		colorPanel2.setBackground(color);
		scanb = new JButton("扫描转换填充");
		boundaryl1 = new JLabel("递归区域填充");
		boundaryl2 = new JLabel("扫描线区域填充");
		boundaryr1 = new JRadioButton();
		boundaryr2 = new JRadioButton();
		ButtonGroup bg = new ButtonGroup(); //按钮组
		bg.add(boundaryr1);
		bg.add(boundaryr2);
		colorb2 = new JButton("选择颜色");
		speedb2 = new JButton("速度调节:ms");
		modeb2 = new JButton("切换模式");
		exitb2 = new JButton("退出");
		seedl = new JLabel("种子点:");
		speedt2 = new JTextField(4);
		speedt2.setText(delay+"");
				
		//添加监听器(填充图元)
		numberb.addActionListener(this);
		scanb.addActionListener(this);
		colorb2.addActionListener(this);
		speedb2.addActionListener(this);
		modeb2.addActionListener(this);
		exitb2.addActionListener(this);
				
		//设置按钮颜色(填充图元)
		scanb.setForeground(Color.blue);
		colorb2.setForeground(color);
		speedb2.setForeground(Color.red);
		modeb2.setForeground(Color.red);
		exitb2.setForeground(Color.red);
				
		//将控件添加到面板(填充图元)
		pn2 = new JPanel(new GridLayout(3,5));
		pn2.add(numberl);
		pn2.add(numbert); pn2.add(numberb);
		pn2.add(speedt2); pn2.add(speedb2);
		pn2.add(boundaryl1); pn2.add(boundaryr1);
		pn2.add(boundaryl2); pn2.add(boundaryr2);
		pn2.add(seedl); pn2.add(scanb);
		pn2.add(colorPanel2); pn2.add(colorb2);
		pn2.add(modeb2);
		pn2.add(exitb2);
	}
	
	public GraphicsGenerator() //窗口的构造方法
	{
		super("基本图元绘制");
		
		//绘制窗口
		this.setSize(600, 800);
		this.setLocation(300,300);
		this.setLayout(new FlowLayout());
		
		this.init_panel(); //初始化线画图元面板
		this.init_panel2(); //初始化填充图元面板
		this.add(pn, "North"); //将控件区域添加到窗口
		
		g = new GPanel();
		g.addMouseListener(this); //添加鼠标监听器，以便交互
		this.add(g, "South"); //将绘制区域添加到窗口
		
		this.setVisible(true);
		this.setResizable(false); //无法改变窗口大小
		timer = new Timer(delay, this); //定义定时器
	}
	
	private void DDA(Point start, Point end)
	{
		//默认起始点位于左侧
		int dx = end.x - start.x; //水平差值
		int dy = end.y - start.y; //垂直差值
		int times; //循环次数
		float inc_x, inc_y; //x,y方向步长
		
		if (dx == 0) //斜率不存在
		{
			times = Math.abs(dy);
			inc_x = 0;
			if (dy > 0) inc_y = 1;
			else inc_y = -1;
		}
		else //斜率存在
		{
			if (Math.abs(dx) > Math.abs(dy)) //斜率绝对值小于1
			{
				times = Math.abs(dx);
				inc_x = 1; //取x方向步长为1
				inc_y = Math.abs((float)dy/dx); //y方向步长为斜率m
			}
			else //斜率绝对值大于1
			{
				times = Math.abs(dy);
				inc_y = 1; //y方向步长为1
				inc_x = Math.abs((float)dx/dy); //x方向步长为1/m
			}
			if (dx < 0) inc_x = -inc_x;
			if (dy < 0) inc_y = -inc_y;
		}
		
		float x = start.x, y = start.y; //初始值
		for (int i = 0; i < times; i++)
		{
			DDAline.add(new Point((int)x, (int)y)); //取整，添加
			x += inc_x;
			y += inc_y;
		}
	}
	
	private void Bresenham(Point start, Point end)
	{
		//默认起始点位于左侧，支持任意斜率
		int dx = end.x - start.x; //水平差值
		int dy = end.y - start.y; //垂直差值
		
		int inc_y = dy > 0 ? 1 : -1;
		int inc_x = dx > 0 ? 1 : -1;//x,y增量
		dx = Math.abs(dx);
		dy = Math.abs(dy);
		int times = dx > dy ? dx : dy; //循环次数
		
		if (dx == 0) //斜率不存在
		{
			int x = start.x, y = start.y; //初始值
			for (int i = 0; i < times; y += inc_y, i++)
				Bresenhamline.add(new Point(x, y));
		}
		else //斜率存在
		{	
			int x = start.x, y = start.y; //初始值
			Bresenhamline.add(new Point(x, y));
			
			if (dx > dy) //斜率绝对值小于1
			{
				int inc_p1 = 2*dy, inc_p2 = 2*dy-2*dx; //两种决策参数增量
				int p = 2*dy - dx; //初始决策参数
				
				for (int i = 0; i < times; i++)
				{
					if(p < 0) //绘制低像素
					{
						x += inc_x;
						Bresenhamline.add(new Point(x, y));
						p += inc_p1; //更新决策参数
					}
					else //绘制高像素
					{
						x += inc_x; y += inc_y;
						Bresenhamline.add(new Point(x, y));
						p += inc_p2; //更新决策参数
					}
				}
			}
			else //斜率绝对值大于1
			{
				int inc_p1 = 2*dx, inc_p2 = 2*dx-2*dy; //两种情况下决策参数增量
				int p = 2*dx - dy; //初始决策参数
				
				for (int i = 0; i < times; i++)
				{
					if(p < 0) //绘制低像素
					{
						y += inc_y;
						Bresenhamline.add(new Point(x, y));
						p += inc_p1; //更新决策参数
					}
					else //绘制高像素
					{
						y += inc_y; x += inc_x;
						Bresenhamline.add(new Point(x, y));
						p += inc_p2; //更新决策参数
					}
				}
			}
		}
	}
	
	private void BresenhamCircle(Point center, int radius)
	{
		int cx = center.x, cy = center.y;
		int x = 0, y = radius; //初始点坐标
		double p = 1.25-radius; //初始决策参数
		while (x <= y)
		{
			Bresenhamcircle.add(new Point(y+cx, x+cy));
			Bresenhamcircle.add(new Point(x+cx, -y+cy));
			Bresenhamcircle.add(new Point(y+cx, -x+cy));
			Bresenhamcircle.add(new Point(-x+cx, -y+cy));
			Bresenhamcircle.add(new Point(-x+cx, y+cy));
			Bresenhamcircle.add(new Point(-y+cx, x+cy));
			Bresenhamcircle.add(new Point(-y+cx, -x+cy));
			if (p < 0) //选取高像素
			{
				x++;
				Bresenhamcircle.add(new Point(x+cx, y+cy));
				p += 2 * (x-1) + 3;
			}
			else
			{
				x++; y--;
				Bresenhamcircle.add(new Point(x+cx, y+cy));
				p += 2 * x - 2 * (y+1) + 5;
			}
		}
	}
	
	private void BresenhamOval(Point center, int rx, int ry)
	{
		int cx = center.x, cy = center.y;
		int x = 0, y = ry; //初始点坐标
		double p1 = ry*ry - rx*rx*ry + rx*rx/4.0; //区域1决策参数初始值
		while (ry*ry*x < rx*rx*y)
		{
			Bresenhamoval.add(new Point(x+cx, -y+cy)); //加入对称点并平移
			Bresenhamoval.add(new Point(-x+cx, y+cy));
			Bresenhamoval.add(new Point(-x+cx, -y+cy));
			if (p1 < 0) //选取高像素
			{
				x++;
				Bresenhamoval.add(new Point(x+cx, y+cy));
				p1 += 2*ry*ry*(x-1) + 3*ry*ry;
			}
			else //选取低像素
			{
				x++; y--;
				Bresenhamoval.add(new Point(x+cx, y+cy));
				p1 += 2*ry*ry*(x-1) - 2*rx*rx*(y+1) + 2*rx*rx + 3*ry*ry;
			}
		}
		
		double p2 = ry*ry*(x+0.5)*(x+0.5) + rx*rx*(y-1) - rx*rx*ry*ry; //区域2决策参数初始值
		while (y >= 0)
		{
			Bresenhamoval.add(new Point(x+cx, -y+cy)); //加入对称点并平移
			Bresenhamoval.add(new Point(-x+cx, y+cy));
			Bresenhamoval.add(new Point(-x+cx, -y+cy));
			if (p2 > 0) //选取高像素
			{
				y--;
				if (y > 0)
					Bresenhamoval.add(new Point(x+cx, y+cy));
				p2 -= 2*rx*rx*(y+1) + 3*rx*rx;
			}
			else //选取低像素
			{
				x++; y--;
				if (y > 0)
					Bresenhamoval.add(new Point(x+cx, y+cy));
				p2 += 2*ry*ry*(x-1) - 2*rx*rx*(y+1) + 2*ry*ry + 3*rx*rx;
			}
		}
	}
	
	public void getVertexs() //生成预设的n边形顶点集,最多6个
	{
		Vertexs.clear();
		List<Point> allpoints = new ArrayList<Point>();
		allpoints.add(new Point(20,20));
		allpoints.add(new Point(10,30));
		allpoints.add(new Point(20,40));
		allpoints.add(new Point(40,40));
		allpoints.add(new Point(50,30));
		allpoints.add(new Point(40,20));
		for (int i = 0; i < number; i++)
			Vertexs.add(allpoints.get(i));
	}
	
	public void getEdge() //计算出多边形边组成的点集
	{
		Edges.clear();
		for (int i = 0; i < number-1; i++)
		{
			Bresenham(Vertexs.get(i), Vertexs.get(i+1)); //求得一条边
			for (Point p:Bresenhamline)
				Edges.add(p); //添加到边集
			Bresenhamline.clear();
		}
		Bresenham(Vertexs.get(number-1), Vertexs.get(0));
		for (Point p:Bresenhamline)
			Edges.add(p); //添加最后一条边
		Bresenhamline.clear();
	}
	
	public boolean isInnerPoint(int x, int y) //采用奇偶规则判断是否区域内点
	{
		int count = 0;
		boolean flag = false; //防止连续边界点影响计数
		for (Point p : Edges)
			if (p.getY() == y && p.getX() > x && !flag) //水平向右引一条射线
			{
				count++;
				flag = true;
			}
			else
				flag = false;
		if (count % 2 == 0)
			return false;
		else
			return true;
	}
	
	public void boundaryFill(int x, int y) //递归边界填充
	{
		//内部区域为4-连通
		//如果当前点是边界或已填充，直接返回
		for (Point p : Edges)
			if (p.getX()==x && p.getY()==y)
				return;
		for (Point p : FillPoints)
			if (p.getX()==x && p.getY()==y)
				return;
		//否则填充
		FillPoints.add(new Point(x, y));
		boundaryFill(x+1, y);
		boundaryFill(x-1, y);
		boundaryFill(x, y+1);
		boundaryFill(x, y-1);
	}
	
	public void boundaryFill2(int x, int y) //扫描线边界填充
	{
		boolean[][] map = new boolean[60][60];
		for (int i = 0; i < 60; i++) //初始化map
			for (int j = 0; j < 60; j++)
				map[i][j] = false;
		for (Point p : Edges)
			map[(int)p.getX()][(int)p.getY()] = true;
		
		Stack<Point> stack = new Stack<Point>();
		stack.push(new Point(x, y)); //种子点入栈
		while (!stack.isEmpty())
		{
			Point p = stack.pop(); //出栈
			int px = (int)p.getX(), py = (int)p.getY(), xl, xr;
			while(map[px][py]==false) //向左填充到边界
			{
				map[px][py] = true;
				FillPoints.add(new Point(px, py));
				px--;
			}
			xl = px + 1;
			
			px = (int)p.getX()+1;
			while(map[px][py]==false) //向右填充到边界
			{
				map[px][py] = true;
				FillPoints.add(new Point(px, py));
				px++;
			}
			xr = px - 1;
			
			Set<Integer> upx = new HashSet<Integer>(); //集合，存放上扫描线最左点
			Set<Integer> downx = new HashSet<Integer>(); //集合，存放下扫描线最左点
			for (int i = xl; i <= xr; i++)
			{
				int minx = i, miny = py+1;
				if (map[minx][miny]==false) //相邻上方点
				{
					while (map[minx-1][miny]==false)
						minx--; //搜索到最左边
					upx.add(minx);
				}
				minx = i; miny = py-1;
				if (map[minx][miny]==false) //相邻下方点
				{
					while (map[minx-1][miny]==false)
						minx--; //搜索到最左边
					downx.add(minx);
				}
				//下一步的点入栈
				for (int sx : upx) 
					stack.add(new Point(sx, py+1));
				for (int sx : downx)
					stack.add(new Point(sx, py-1));
			}
		}
	}
	
	public void initEdgeTable() //根据顶点集初始化边表
	{
		for (int i = 0; i < 60; i++) //初始化空边表
			edgeTable.add(new ArrayList<ET>());
		double ymin, ymax, xmin, xmax, dx, dy;
		for (int i = 0; i < number-1; i++)
		{
			Point pa = Vertexs.get(i);
			Point pb = Vertexs.get(i+1);
			if (pa.getY() > pb.getY()) //a是高点
			{
				ymax = pa.getY(); ymin = pb.getY();
				xmax = pa.getX(); xmin = pb.getX();
			}
			else
			{
				ymax = pb.getY(); ymin = pa.getY();
				xmax = pb.getX(); xmin = pa.getX();
			}
			dx = xmax - xmin;
			dy = ymax - ymin;
			if (dy != 0)
				edgeTable.get((int)ymin).add(new ET(ymax, dx/dy, xmin)); //按y最低分类，插入边表
		}
		//处理最后一条边
		Point pa = Vertexs.get(number-1);
		Point pb = Vertexs.get(0);
		if (pa.getY() > pb.getY()) //a是高点
		{
			ymax = pa.getY(); ymin = pb.getY();
			xmax = pa.getX(); xmin = pb.getX();
		}
		else
		{
			ymax = pb.getY(); ymin = pa.getY();
			xmax = pb.getX(); xmin = pa.getX();
		}
		dx = xmax - xmin;
		dy = ymax - ymin;
		if (dy != 0)
			edgeTable.get((int)ymin).add(new ET(ymax, dx/dy, xmin)); //按y最低分类，插入边表
	}
	
	public void scanConversion()
	{
		edgeTable.clear();
		activeEdgeTable.clear();
		initEdgeTable(); //初始化边表
		int y = 0;
		while (y < 60) //对于每条扫描线循环
		{
			if (edgeTable.get(y).size() != 0) //若该扫描线位置边表不空
				for (ET e: edgeTable.get(y)) //全部加入AET
					activeEdgeTable.add(new AET(e.ymax, e.dx, e.xmin)); 
			
			//按x从小到大排序，若x相同，按步长从小到大排序
			Collections.sort(activeEdgeTable, new Comparator<AET>(){
	            public int compare(AET o1, AET o2)
	            {
					if (o1.x > o2.x) //先按x排序
						return (int)(o1.x - o2.x);
					if (o1.x < o2.x)
						return (int)(o1.x - o2.x);
					if (o1.dx > o2.dx) //x相同，按dx排序
						return 1;
					if (o1.dx < o2.dx)
						return -1;
					return 0;
	            }
	        });
			
			int tablesize = activeEdgeTable.size(), count = 0;
			AET temp = null;
			if (tablesize != 0 && tablesize % 2 == 0) //两个一组，将x之间的位置填充
				for (AET aet : activeEdgeTable)
				{
					count++;
					if (count == 1)
						temp = aet;
					if (count == 2)
					{
						for (int i = (int)temp.x+1; i < (int)aet.x; i++)
							FillPoints.add(new Point(i, y));
						count = 0;
					}
				}
			
			y++; //纵坐标增加
			
			Iterator<AET> iter = activeEdgeTable.iterator(); //删除所有y == ymax的边
			while(iter.hasNext())
			    if(iter.next().ymax == y)
			        iter.remove();
			
			for (AET aet : activeEdgeTable) //剩下的边将x进行迭代
				aet.x += aet.dx;
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
		
		if (e.getSource()==ddab) //采用DDA绘制
		{
			timer.stop();
			workpoints.clear();
			paintpoints.clear();
			//获取绘制直线所需参数
			int x1 = Integer.parseInt(x1t.getText());
			int y1 = Integer.parseInt(y1t.getText());
			int x2 = Integer.parseInt(x2t.getText());
			int y2 = Integer.parseInt(y2t.getText());
			Point start = new Point(x1, y1);
			Point end = new Point(x2, y2); //直线的端点
			
			DDA(start, end); //使用DDA算法求直线的点集
			workpoints = DDAline;
			timer.start(); //计时器开始
		}
		
		else if (e.getSource()==bresenhamb) //使用Bresenham绘制
		{
			timer.stop();
			workpoints.clear();
			paintpoints.clear();
			//获取绘制直线所需参数
			int x1 = Integer.parseInt(x1t.getText());
			int y1 = Integer.parseInt(y1t.getText());
			int x2 = Integer.parseInt(x2t.getText());
			int y2 = Integer.parseInt(y2t.getText());
			Point start = new Point(x1, y1);
			Point end = new Point(x2, y2); //直线的端点
			
			Bresenham(start, end);
			workpoints = Bresenhamline;
			timer.start(); //计时器开始
		}
		
		else if (e.getSource()==exitb) //退出程序
		{
			System.exit(0);
		}
		
		else if (e.getSource()==circleb) //绘制中点圆
		{
			timer.stop();
			workpoints.clear();
			paintpoints.clear();
			//获取绘制圆的参数
			int x = Integer.parseInt(xt.getText());
			int y = Integer.parseInt(yt.getText());
			int radius = Integer.parseInt(radiust.getText()); //半径
			Point center = new Point(x, y); //圆心
			
			BresenhamCircle(center, radius);
			workpoints = Bresenhamcircle;
			timer.start(); //计时器开始
		}
		
		else if (e.getSource()==ovalb) //绘制椭圆
		{
			timer.stop();
			workpoints.clear();
			paintpoints.clear();
			//获取绘制椭圆的参数
			int x = Integer.parseInt(xt.getText());
			int y = Integer.parseInt(yt.getText());
			int rx = Integer.parseInt(radiusat.getText());
			int ry = Integer.parseInt(radiusbt.getText()); //长短轴
			Point center = new Point(x, y); //圆心
			
			BresenhamOval(center, rx, ry);
			workpoints = Bresenhamoval;
			timer.start(); //计时器开始
		}
		
		else if (e.getSource()==colorb)
		{
			color = JColorChooser.showDialog(GraphicsGenerator.this,"选取颜色",Color.lightGray );  //得到选择的颜色
			if (color == null)
				color = Color.blue; //若未选择，默认为蓝色
			colorPanel.setBackground(color);
			colorb.setForeground(color);
		}
		
		else if (e.getSource()==speedb)
		{
			delay = Integer.parseInt(speedt.getText());
			timer.setDelay(delay);
		}
		
		else if (e.getSource()==numberb)
		{
			number = Integer.parseInt((String)numbert.getSelectedItem());
			getVertexs(); //获取预设的顶点
			getEdge(); //计算多边形的边组成的点集
			g.repaint();
		}
		
		else if (e.getSource()==modeb)
		{
			this.remove(pn);
			this.add(pn2, "North");
			
			getVertexs(); //获取预设的顶点
			getEdge(); //计算多边形的边组成的点集
			workmode = true;
			
			this.add(g, "South");
			this.validate();
			this.repaint();
		}
		
		else if (e.getSource()==modeb2)
		{
			this.remove(pn2);
			this.add(pn, "North");
			g.setContent(Bresenhamline, color);
			this.add(g, "South");
			workmode = false;
			this.validate();
			this.repaint();
		}
		
		else if (e.getSource()==exitb2) //退出程序
		{
			System.exit(0);
		}
		
		else if (e.getSource()==colorb2)
		{
			color = JColorChooser.showDialog(GraphicsGenerator.this,"选取颜色",Color.lightGray );  //得到选择的颜色
			if (color == null)
				color = Color.blue; //若未选择，默认为蓝色
			colorPanel2.setBackground(color);
			colorb2.setForeground(color);
		}
		
		else if (e.getSource()==speedb2)
		{
			delay = Integer.parseInt(speedt2.getText());
			timer.setDelay(delay);
		}
		
		else if (e.getSource()==scanb)
		{
			FillPoints.clear();
			scanConversion(); //扫描转换填充
			timer.stop();
			workpoints = FillPoints;
			timer.start(); //绘制当前已经填充的点
		}
		
		else if (e.getSource()==timer) //计时器
		{
			if (!workpoints.isEmpty())
			{
				paintpoints.add(workpoints.get(0));
				workpoints.remove(0);
				g.setContent(paintpoints, color);
				g.repaint();
			}
			else
			{
				timer.stop();
				paintpoints.clear();
			}
		}
	}
	
	//处理鼠标事件
	public void mousePressed(MouseEvent e) //按下与释放共同实现拖拽功能
	{
		if (e.getButton() == MouseEvent.BUTTON1) //按下左键
		{
			Point location = e.getPoint();
			int x = (int)Math.floor(location.getX()/10.0);
			int y = (int)Math.floor(location.getY()/10.0);
			//System.out.println(x+" "+y);
			for (int i = 0; i < number; i++)
			{
				if (Vertexs.get(i).getX() == x && Vertexs.get(i).getY() == y) //若点击的是顶点，开始修改顶点
				{
					clicked = true;
					vertexindex = i; // 记录删除的位置
					Vertexs.remove(i);
					break;
				}
			}
		}
		else if (e.getButton() == MouseEvent.BUTTON3) //按下右键，区域填充，假设内部为4连通
		{
			FillPoints.clear();
			Point location = e.getPoint();
			int x = (int)Math.floor(location.getX()/10.0);
			int y = (int)Math.floor(location.getY()/10.0);
			if (!isInnerPoint(x, y)) //如果不是内点
				JOptionPane.showMessageDialog(null, "选择的种子点不是内点！", "错误", JOptionPane.ERROR_MESSAGE);
			else //否则填充
			{
				seedl.setText("种子点:("+x+","+y+")");
				if (boundaryr1.isSelected())
					boundaryFill(x, y); //递归边界填充
				if (boundaryr2.isSelected())
					boundaryFill2(x, y); //扫描线边界填充
				timer.stop();
				workpoints = FillPoints;
				timer.start(); //计时器开始
			}
		}
	}

	public void mouseReleased(MouseEvent e)
	{
		if (clicked) //若已经点击顶点
		{
			clicked = false;
			Point location = e.getPoint();
			int x = (int)Math.floor(location.getX()/10.0);
			int y = (int)Math.floor(location.getY()/10.0);
			Vertexs.add(vertexindex, new Point(x, y)); //添加释放时鼠标位置的点
			getEdge(); //重新计算边
			g.repaint();
		}
	}
	
	public void mouseClicked(MouseEvent e)
	{
		
	}
	
	public void mouseEntered(MouseEvent e)
	{
		
	}

	public void mouseExited(MouseEvent e)
	{
		
	}
	
	public static void main(String[] args)
	{
		new GraphicsGenerator();
	}

}


class GPanel extends JPanel
{
	List<Point> points = new ArrayList<Point>(); //求得的点
	Color color; //绘制颜色
	int gridwidth = 10; //单个网格的宽度
	
	public void setContent(List<Point> points, Color color)
	{
		this.points = points;
		this.color = color;
	}
	
	public GPanel()
	{
		this.setPreferredSize(new Dimension(600, 600));
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		int x = 0, y = 0;
		for (int i = 0; i < 60; i++) //绘制网格
		{
			for (int j = 0; j < 60; j++)
			{
				g.drawRect(x, y, gridwidth, gridwidth);
				x = x + gridwidth;
			}
			x = 0;
			y = y + gridwidth;
		}
		if (!GraphicsGenerator.workmode) //线画图元
		{
			g.setColor(color);
			for (Point p : points) //遍历列表，绘制指定点
				g.fillRect(gridwidth*p.x, gridwidth*p.y, gridwidth, gridwidth);
		}
		else //填充图元
		{
			g.setColor(Color.gray);
			for (Point p : GraphicsGenerator.Edges) //绘制多边形的边，灰色
				g.fillRect(gridwidth*p.x, gridwidth*p.y, gridwidth, gridwidth);
			g.setColor(Color.orange);
			for (Point p : GraphicsGenerator.Vertexs) //绘制多边形的顶点，橙色
				g.fillRect(gridwidth*p.x, gridwidth*p.y, gridwidth, gridwidth);
			g.setColor(color);
			for (Point p : points) //绘制内部点
				g.fillRect(gridwidth*p.x, gridwidth*p.y, gridwidth, gridwidth);
		}
	}
}

class ET //有序边表
{
	public double ymax; //边最高点的y值
	public double dx; //斜率的倒数
	public double xmin; //边最低点的x值
	
	public ET(double ymax, double dx, double xmin)
	{
		this.ymax = ymax;
		this.dx = dx;
		this.xmin = xmin;
	}
	
	public String toString()
	{
		return ymax+" "+dx+" "+xmin;
	}
}

class AET//活化边表
{
	public double ymax; //边最高点的y值
	public double dx; //斜率的倒数
	public double x; //扫描线和边的交点横坐标
	
	public AET(double ymax, double dx, double x)
	{
		this.ymax = ymax;
		this.dx = dx;
		this.x = x;
	}
	
	public String toString()
	{
		return ymax+" "+dx+" "+x;
	}
}