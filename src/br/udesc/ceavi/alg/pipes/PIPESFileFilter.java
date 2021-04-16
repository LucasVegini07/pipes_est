package br.udesc.ceavi.alg.pipes;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 */
public class PIPESFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String extension = f.getName().replaceAll(".+\\.(\\w+)", "$1");
        if (extension != null) {
            return extension.toLowerCase().equals("pipes");
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "Arquivo de jogo PIPES";
    }
    
}
