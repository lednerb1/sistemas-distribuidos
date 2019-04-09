
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.util.List;

import java.io.IOException;
import java.io.File;

public class GerenciadorArquivo{

  public static String diretorio;
  private String nomeArquivo, extensao;
  private int nroLinha=0;

  public GerenciadorArquivo(String diretorio, String nomeArquivo, String extensao) throws IOException{
    this.diretorio=diretorio+"/"; this.nomeArquivo=nomeArquivo;
    this.extensao=extensao; this.nroLinha=0;
    if(!criarArquivo()){
      System.out.println("Um erro ocorreu ao inicializar o arquivo "+ diretorio+nomeArquivo+extensao);
    }
  }

  public GerenciadorArquivo(String nomeArquivo, String extensao) throws IOException{
    this.nomeArquivo=nomeArquivo;
    this.extensao=extensao;
    this.nroLinha=0;
    this.diretorio="";

    if(!criarArquivo()){
      System.out.println("Um erro ocorreu ao inicializar o arquivo "+ diretorio+nomeArquivo+extensao);
    }
  }

  public void escrever(String msg) throws IOException {
    // List<String> list = Files.readAllLines(Paths.get(diretorio+nomeArquivo+extensao)); ker
    nroLinha++;
    Files.write(Paths.get(diretorio+nomeArquivo+extensao), msg.getBytes(), StandardOpenOption.APPEND);//escrever
  }

  // public String ler() throws IOException{//le uma linha
  //
  //
  //
  //   //deve existir antes pois estou apenas testando
  //   List<String> list = Files.readAllLines(Paths.get(diretorio+nomeArquivo+extensao));
  //
  //   nroLinha +=1;
  //   if(nroLinha-1 < list.size())
  //   return list.get(nroLinha-1);
  //
  //   return null;
  // }

  public boolean criarArquivo() throws IOException{//verificar se ja existe ...
    File f =new File(diretorio+nomeArquivo+extensao);

    if(f.exists()){
      return true;
    }

    return f.createNewFile();
  }

  public boolean renomearArquivo(String novoNome) throws IOException{//verificar como renomear
    this.nomeArquivo=novoNome;
    return new File(diretorio+nomeArquivo+extensao).renameTo(new File(diretorio+novoNome+extensao));
  }

  public boolean deletarArquivo() throws IOException{
    return new File(diretorio+nomeArquivo+extensao).delete();
  }

  public int qtdLinhasAtual() throws IOException{
    return Files.readAllLines(Paths.get(diretorio+nomeArquivo+extensao)).size();
  }

}
