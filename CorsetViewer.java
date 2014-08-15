import com.jogamp.opengl.util.*;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.media.opengl.fixedfunc.*;


public class CorsetViewer implements GLEventListener, MouseMotionListener {

    private Pattern[] corset;
    private Corset corset2;
    private GLU glu;
    private float distance = 100;
    private double angleY = 0;
    private double angleX = Math.PI/2;
    private String textureName;

    public CorsetViewer(Pattern[] corset, Corset corset2, String textureName) {
        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        GLCanvas canvas = new GLCanvas(caps);
        this.corset = corset;
        this.corset2 = corset2;
        this.textureName = textureName;

        Frame frame = new Frame("Corset Viewer");
        frame.setSize(600, 600);
        frame.add(canvas);
        frame.setVisible(true);
        
        canvas.addMouseMotionListener(this);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        canvas.addGLEventListener(this);

        FPSAnimator animator = new FPSAnimator(canvas, 120);
        animator.start();
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        update();
        render(drawable);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    @Override
    public void init(GLAutoDrawable drawable) {
      glu = new GLU();
      GL2 gl = drawable.getGL().getGL2();
      corset2.addTexture(gl, textureName);
      gl.glEnable(GL.GL_CULL_FACE);
      gl.glCullFace(GL.GL_FRONT);
      gl.glEnable(GL.GL_DEPTH_TEST);
      gl.glDepthMask(true);
      gl.glDepthFunc(GL.GL_LEQUAL);
      gl.glDepthRange(0.0f, 1.0f);
      setCamera(gl, glu, 0, 100, distance);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
      GL gl = drawable.getGL();
      gl.glViewport(0, 0, w, h);
    }

    private void update() {
    }

    private void render(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClearDepth(1);
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
	setCamera(gl, glu, angleX, angleY, distance);
        
        for(int i=0; i<corset.length; ++i)
	  corset[i].draw(gl);
    }
    
    @Override
    public void mouseDragged(MouseEvent event){
      angleY = Math.PI - event.getY()*Math.PI/300;
      angleX = event.getX()*Math.PI/300 - Math.PI/2;
    }

    @Override
    public void mouseMoved(MouseEvent event){
    }
    
    private void setCamera(GL2 gl, GLU glu, double angle, double height, float distance) {
        // Change to projection matrix.
        gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
        gl.glLoadIdentity();

        // Perspective.
        float widthHeightRatio = 1;
        glu.gluPerspective(45, widthHeightRatio, 0, 10000);
        glu.gluLookAt(
	  distance*Math.cos(angleX)*Math.cos(angleY), 
	  distance*Math.cos(angleX)*Math.sin(angleY), 
	  distance*Math.sin(angleX), 
	  0, 0, 0, 0, 1, 0);

        // Change back to model view matrix.
        gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
}
