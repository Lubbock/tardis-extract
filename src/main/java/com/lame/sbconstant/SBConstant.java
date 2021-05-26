package com.lame.sbconstant;


import com.lame.sbconstant.command.Command;
import com.lame.sbconstant.command.Context;
import com.lame.sbconstant.command.SimCommand;
import com.lame.sbconstant.command.XTFCommand;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class SBConstant {
    static final String banner = "\033[36;4m" + " #####  ######   #####                                                 \n" +
            "#     # #     # #     #  ####  #    #  ####  #####   ##   #    # ##### \n" +
            "#       #     # #       #    # ##   # #        #    #  #  ##   #   #   \n" +
            " #####  ######  #       #    # # #  #  ####    #   #    # # #  #   #   \n" +
            "      # #     # #       #    # #  # #      #   #   ###### #  # #   #   \n" +
            "#     # #     # #     # #    # #   ## #    #   #   #    # #   ##   #   \n" +
            " #####  ######   #####   ####  #    #  ####    #   #    # #    #   #   \n" + "\033[0m";

    public static boolean aiDetect;

    public static boolean saveFile = true;

    public static String output = "./";

    public static Worker[] workers = new Worker[3];

    public static boolean threaded = false;

    static int windex = 0;

    public static CyclicBarrier barrier;

    public static String command = "default";

    public static volatile boolean firstPassDone = false;


    public static class Worker implements Runnable {
        public long parserStart;
        public long parserStop;
        List<String> files;

        public Worker(List<String> files) {
            this.files = files;
        }

        @Override
        public void run() {
            parserStart = System.currentTimeMillis();
            for (String f : files) {
                parseFile(f);
            }
            parserStop = System.currentTimeMillis();
            try {
                barrier.await();
            } catch (InterruptedException ex) {
                return;
            } catch (BrokenBarrierException ex) {
                return;
            }
        }
    }

    public static void doFiles(List<String> files) throws Exception {
        long parseStart = System.currentTimeMillis();
        if (threaded) {
            barrier = new CyclicBarrier(3, new Runnable() {
                @Override
                public void run() {
                    report();
                    firstPassDone = true;
                }
            });
            int chunkSize = files.size() / 3;
            int p1 = chunkSize;
            int p2 = 2 * chunkSize;
            workers[0] = new Worker(files.subList(0, p1 + 1));
            workers[1] = new Worker(files.subList(p1 + 1, p2 + 1));
            workers[2] = new Worker(files.subList(p2 + 1, files.size()));
            new Thread(workers[0], "worker-" + windex++).start();
            new Thread(workers[1], "worker-" + windex++).start();
            new Thread(workers[2], "worker-" + windex++).start();
        } else {
            for (String file : files) {
                parseFile(file);
            }
        }
    }

    public static List<String> getFilenames(File f) throws Exception {
        List<String> files = new ArrayList<String>();
        getFilenames_(f, files);
        return files;
    }

    public static void getFilenames_(File f, List<String> files) throws Exception {
        // If this is a directory, walk each file/dir in that directory
        if (f.isDirectory()) {
            String flist[] = f.list();
            for (int i = 0; i < flist.length; i++) {
                getFilenames_(new File(f, flist[i]), files);
            }
        }

        // otherwise, if this is a java file, parse it!
        else if (((f.getName().length() > 5) &&
                f.getName().substring(f.getName().length() - 5).equals(".java"))) {
            files.add(f.getAbsolutePath());
        }
    }

    private static void report() {
        long time = 0;
        if (workers != null) {
            // compute max as it's overlapped time
            for (Worker w : workers) {
                long wtime = w.parserStop - w.parserStart;
                time = Math.max(time, wtime);
                System.out.println("worker time " + wtime + "ms.");
            }
        }
        System.out.println("Total lexer+parser time " + time + "ms.");

        System.out.println("finished parsing OK");
    }

    public static void parseFile(String f) {
        try {
            Context context = new Context(aiDetect, saveFile, output);
            Command simCommand = new SimCommand();
            switch (command) {
                case "xtf":
                    simCommand = new XTFCommand();
                    break;
                default:
                    break;
            }
            simCommand.execute(context, f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println(banner);
        String fp = "";
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-ai":
                        aiDetect = true;
                        break;
                    case "-nosave":
                        saveFile = false;
                        break;
                    case "-f":
                        i++;
                        fp = args[i];
                        break;
                    case "-threaded":
                        threaded = true;
                        break;
                    case "-o":
                        i++;
                        output = args[i];
                        break;
                    case "-command":
                        i++;
                        command = args[i];
                        break;
                    default:
                        System.out.println("参数不正确");
                        return;
                }
            }
        }
        File nf = new File(fp);
        List<String> files = new ArrayList<>();
        if (nf.isDirectory()) {
            SuffixFileFilter suffixFileFilter = new SuffixFileFilter(".java");
            Collection<File> cfs = FileUtils.listFiles(nf, suffixFileFilter, TrueFileFilter.TRUE);
            for (File cf : cfs) {
                files.add(cf.getAbsolutePath());
            }
        } else {
            if (fp.endsWith(".java")) {
                files.add(fp);
            } else {
                System.out.println("文件类型错误");
                return;
            }
        }
        try {
            doFiles(files);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
