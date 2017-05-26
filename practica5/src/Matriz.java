import java.util.Random;

class Matriz {

    private int[][] m1;
    private int[][] m2;
    private int[][] res;
    private Hilo[] hilos;
    
    public Matriz(int a, int b, int c, int n) {
        m1 = new int[a][b];
        m2 = new int[b][c];
        res = new int[a][c];
        hilos = new Hilo[n];
        initMatrices();
        
        int colsPorHilo = a / n;
        for (int x = 0; x < n; x++) {
            int from = x * colsPorHilo;
            int to = (x + 1) * colsPorHilo;
            if(x == n - 1)
                hilos[x] = new Hilo(from, a);
            else hilos[x] = new Hilo(from, to);
        }
        
        for (Hilo h: hilos)
            h.start();

        for (int x = 0; x < hilos.length; x++) {
            try {
                hilos[x].join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        
        System.out.println("M1 * M2 res");
        System.out.println(str(res));
    }

    private void initMatrices() {
        Random r = new Random();
        for(int i = 0; i < m1.length; i++)
            for(int j = 0; j < m1[0].length; j++) {
                m1[i][j] = r.nextInt(1000);
            }
        for(int i = 0; i < m2.length; i++)
            for(int j = 0; j < m2[0].length; j++) {
                m2[i][j] = r.nextInt(1000);
            }
        System.out.println("M1");
        System.out.println(str(m1));
        System.out.println("M2");
        System.out.println(str(m2));
    }

    private String str(int[][] m) {
        String s = "";
        for(int i = 0; i < m.length; i++) {
            for(int j = 0; j < m[0].length; j++) {
                s += m[i][j];
                if(j < m[0].length - 1)
                    s += " ";
            }
            s += "\n";
        }
        return s;
    }
    
    private class Hilo extends Thread {

        private final int from;
        private final int to;
        
        public Hilo(int from, int to) {
            this.from = from;
            this.to = to;
        }
        
        @Override
        public void run() {      
            for (int i = from; i < to; i++) {
                for(int j = 0; j < m2[0].length; j++) {
                    int x = 0;
                    for(int k = 0; k < m1[0].length; k++)
                        x += m1[i][k] * m2[k][j];
                    res[i][j] = x;
                }
            }
        }           
    }
    
}
