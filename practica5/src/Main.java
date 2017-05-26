public class Main {

    public static void main(String args[]) {
        if(args.length < 4)
            // M1<n1,m1> , M2<n2,m2>
            System.out.println("Uso: java Main <n1> <m1 y n2> <m2> <Núm. de hilos>");
        else {
            int n = Integer.parseInt(args[3]);
            int a = Integer.parseInt(args[0]);
            if(a < n)
                System.out.println("Número de hilos no puede ser mayor a número de renglones de matríz 1");
            else {
                Matriz m = new Matriz(a, Integer.parseInt(args[1]), Integer.parseInt(args[2]), n);
            }
        }
    }
}
