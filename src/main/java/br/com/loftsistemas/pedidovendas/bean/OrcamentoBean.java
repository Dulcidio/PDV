/**
 * @author Dulcidio Sobrinho
 * 22/01/2019
 */
package br.com.loftsistemas.pedidovendas.bean;

 
 
import java.io.ByteArrayInputStream;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
 
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.sql.Connection;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.servlet.http.HttpServletResponse;
import javax.swing.InputMap;
import javax.swing.JOptionPane;

import org.omnifaces.util.Messages;
 
import org.primefaces.model.chart.PieChartModel;
import org.primefaces.model.diagram.connector.StateMachineConnector.Orientation;

import br.com.adilson.util.Extenso;
import br.com.adilson.util.PrinterMatrix;
import br.com.loftsistemas.pedidovendas.dao.ClienteDao;
import br.com.loftsistemas.pedidovendas.dao.FuncionarioDao;
import br.com.loftsistemas.pedidovendas.dao.LancamentosCaixaDao;
import br.com.loftsistemas.pedidovendas.dao.OrcamentoDao;
import br.com.loftsistemas.pedidovendas.dao.ProdutoDao;
import br.com.loftsistemas.pedidovendas.dao.RegistroDao;
import br.com.loftsistemas.pedidovendas.dao.ServicosOrcamentosDao;
import br.com.loftsistemas.pedidovendas.dao.UsuarioDao;
import br.com.loftsistemas.pedidovendas.dominio.Caixa;
import br.com.loftsistemas.pedidovendas.dominio.Cliente;
import br.com.loftsistemas.pedidovendas.dominio.Funcionario;
import br.com.loftsistemas.pedidovendas.dominio.LancamentosCaixa;
import br.com.loftsistemas.pedidovendas.dominio.Orcamento;
import br.com.loftsistemas.pedidovendas.dominio.Produto;
import br.com.loftsistemas.pedidovendas.dominio.Registro;
import br.com.loftsistemas.pedidovendas.dominio.ServicosOrcamento;
import br.com.loftsistemas.pedidovendas.dominio.SituacaoOrcamento;
import br.com.loftsistemas.pedidovendas.dominio.Usuario;
import br.com.loftsistemas.pedidovendas.util.HibernateUtil;
import br.com.loftsistemas.pedidovendas.util.UtilRelatorios;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.export.JRTextExporter;

/**
 * @author Dulcidio Sobrinho
 *
 */

@SuppressWarnings("serial")
@ManagedBean (name="orcamentoBean")
@SessionScoped
public class OrcamentoBean implements Serializable{
	
	private List<Produto> produtos;
	 
	private List<ServicosOrcamento> servicosOrcamento;
	private List<ServicosOrcamento> produtosOrcamento;
	private OrcamentoDao orcamentoDao = new OrcamentoDao();
	private Orcamento orcamento = new Orcamento();
	private List <Cliente> clientes;
	private List<Funcionario> funcionarios;
	private List<Usuario>usuarios;
	private Cliente cliente = new Cliente();
	private String dataForm; 
	private Usuario usuario = new Usuario();
	private Funcionario funcionario = new Funcionario();
	private FuncionarioDao funcionarioDao = new FuncionarioDao();
    private PieChartModel pizza;
    private List<Orcamento> orcamentos;
    private List<Orcamento> listadeOrcamentos;
    private Long totalG=0L;
    private Long produtoOrca = 0L;
	private ServicosOrcamentosDao servorcaDao = new ServicosOrcamentosDao();
	private List<ServicosOrcamento> servicosOrcamentoPizza;
	private BigDecimal descon;
	private Long numero;
	private Long nroReg;
    private List<String> lista =  new ArrayList<String>();
    private String ativo;
    private String barrasBusca;
    private Long codigoOrcamento;
    private ServicosOrcamento servico =  new ServicosOrcamento();
    private Produto produtoB = new Produto();
    private BigDecimal troco;
    private BigDecimal pago;
    private BigDecimal calculaTroco;
    private int veriDesc;
    private LancamentosCaixa lancamento = new LancamentosCaixa();
    private Caixa caixa = new Caixa();
    private String forma;
    private Cliente tipoReg ;
    private List<Registro> registros;
    private String razao;
    private String cpf;
    private String rua;
    private String nroRua;
    private String fone;
    private String cel;
    String opera;
    private AutenticacaoBean autentica = new AutenticacaoBean();
   {
		lista.add(0,"Ativo");
		lista.add(1,"Desativo");
	}
   
   private Map<String,String> formas = new HashMap<String, String>();
   
   public void pgtoFormas() {
	  formas = new HashMap<String, String>();
	  formas.put("Dinheiro", "Dinheiro");
	  formas.put("Cartão débito", "Cartão débito");
	  formas.put("Cartão crédito", "Cartão crédito");
	  formas.put("Deposito", "Deposito");
	  formas.put("Transferência bancária", "Transferência bancária");
	  formas.put("Boleto", "Boleto");
	  formas.put("Promissória", "Promissória");
	  
   }
   
	public void detalhesOrcamento(){
		mostrarData();
		UsuarioDao uDao =  new UsuarioDao();
		//contar(totalG);
		usuarios=uDao.listar();
		orcamento.setValorTotal(new BigDecimal("0.00"));
		orcamento.setDesconto(0);
		orcamento.setData(new Date());
		servicosOrcamento = new ArrayList<>();
		pgtoFormas(); 
		 
		//produto=new Produto();
		//servicosOrcamentoPizza= servorcaDao.listar();
		//pizza(); 
	}
	
	public void carregaFunfClien() {
		OrcamentoDao oDao = new OrcamentoDao();
		orcamentos=oDao.listar();
		FuncionarioDao fDao = new FuncionarioDao();
	    funcionarios=fDao.listar();
	    ClienteDao cDao = new ClienteDao();
	    clientes=cDao.listar();
	}
	
	public void buscarPorBarras(String barras) {
		try {
		ProdutoDao pDao = new ProdutoDao();
		Produto retorno = pDao.buscarBarras(produtoB.getBarras());
		
		if(retorno == null) {
			Messages.addGlobalError("Produto não encontrado");
		}
		 produtoB=retorno;
		 
		}catch(Exception e) {
		e.printStackTrace();
		}
	}
	public void adicionarProdutoBarras( ){
		buscarPorBarras(barrasBusca);
		Produto produto = produtoB;
		 
        int achou= -1;
		
		for(int posicao=0; posicao < servicosOrcamento.size(); posicao++){
			
			if(servicosOrcamento.get(posicao).getProduto().equals(produto)){
				achou=posicao;
			}
		}
		if(achou < 0 ){
		
	    ServicosOrcamento prodOrca = new ServicosOrcamento();
		prodOrca.setDescricao(produto.getDescricao());
		prodOrca.setValor(produto.getPrecoVista());
		prodOrca.setTipo(produto.getTipo());
		prodOrca.setQuantidade(new Short("1"));
		prodOrca.setProduto(produto);
		
		servicosOrcamento.add(prodOrca);
		mensagemAdicionarProduto(); 
		produtoB= new Produto();
		}else{
			 ServicosOrcamento prodOrca = servicosOrcamento.get(achou);
			 prodOrca.setQuantidade(new Short ( prodOrca.getQuantidade()+ 1 +""));
			 prodOrca.setValor(produto.getPrecoVista().multiply(new BigDecimal(prodOrca.getQuantidade())));
			 mensagemAdicionarProduto();
			 produtoB= new Produto();
		}
		
		calcularTotal();
	}
	
	public void carregaOrcamentos(){
		try{
		OrcamentoDao orcaDao = new OrcamentoDao();
		listadeOrcamentos=orcaDao.listar();
		//servicosOrcamentoPizza= servorcaDao.listar();
		contar(totalG);
		//pizza();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void contar(Long total){
		try{
            total=orcamentoDao.ContarTotal(total);
            totalG=total;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void contarProduto(Long total){
		try{
            total=servorcaDao.ContarTotal(total);
            produtoOrca=total;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void pizza(){
		
		pizza = new PieChartModel();
		
		for(ServicosOrcamento sp:servicosOrcamentoPizza ){
			pizza.set(sp.getDescricao(),sp.getQuantidade());
			
		}
		pizza.setTitle("Produtos e Serviços");
		pizza.setLegendPosition("e");
		pizza.setFill(false);
		pizza.setShowDataLabels(true);
		pizza.setDiameter(100);
		
	}
	
	
	public String mostrarData(){
    	Date data = new Date();
    	SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    	String dataformatada =  formato.format(data);
    	dataForm = dataformatada;
    	return dataForm;
    	
    }
	 
	
	public String getDataForm() {
		return dataForm;
	}

	public void setDataForm(String dataForm) {
		this.dataForm = dataForm;
	}
	


	public void adicionarProduto( ActionEvent evento ){
		Produto produto = (Produto)evento.getComponent().getAttributes().get("produtoSelecionado");
		 
        int achou= -1;
		
		for(int posicao=0; posicao < servicosOrcamento.size(); posicao++){
			
			if(servicosOrcamento.get(posicao).getProduto().equals(produto)){
				achou=posicao;
			}
		}
		if(achou < 0 ){
		
	    ServicosOrcamento prodOrca = new ServicosOrcamento();
		prodOrca.setDescricao(produto.getDescricao());
		prodOrca.setValor(produto.getPrecoVista());
		prodOrca.setTipo(produto.getTipo());
		prodOrca.setQuantidade(new Short("1"));
		prodOrca.setProduto(produto);
		
		servicosOrcamento.add(prodOrca);
		mensagemAdicionarProduto(); 
		
		}else{
			 ServicosOrcamento prodOrca = servicosOrcamento.get(achou);
			 prodOrca.setQuantidade(new Short ( prodOrca.getQuantidade()+ 1 +""));
			 prodOrca.setValor(produto.getPrecoVista().multiply(new BigDecimal(prodOrca.getQuantidade())));
			 mensagemAdicionarProduto();  
		}
		produtosOrcamento=servicosOrcamento;
		calcularTotal();
	}
	
	public void remover(ActionEvent evento){
		ServicosOrcamento items = (ServicosOrcamento) evento.getComponent().getAttributes().get("itemSelecionado");
		
		int achou= -1;
		for(int posicao = 0; posicao < servicosOrcamento.size(); posicao++ ){
			if(servicosOrcamento.get(posicao).getProduto().equals(items.getProduto())){
				achou=posicao;
			}
		}
		if(achou > -1){
			servicosOrcamento.remove(achou);
		}
		calcularTotal();
		troco();
	}
	
	public void calcularTotal(){
		orcamento.setValorTotal(new BigDecimal("0.00"));
		for(int posicao = 0; posicao < servicosOrcamento.size(); posicao++ ){
		ServicosOrcamento items = servicosOrcamento.get(posicao);
		orcamento.setValorTotal(orcamento.getValorTotal().add(items.getValor()));
		
		}
 	}
	
	public void desconto(){
		calcularTotal();
	 
		int desc = orcamento.getDesconto();
		float val =  orcamento.getValorTotal().floatValue();
		float total =0;
		
		 if(desc > 0){
		   total = (val*desc)/100;
		   descon=(new BigDecimal(total));
		   orcamento.setValorTotal(orcamento.getValorTotal().subtract(descon).setScale(2,RoundingMode.DOWN));
		   calculaTroco=orcamento.getValorTotal().subtract(descon).setScale(2,RoundingMode.DOWN);
		 }else{
			 calcularTotal();
		 }
		cleanDescont();
		total=0;
		val=0;
		desc=0;
		 
	}
	
	public void troco(){
		BigDecimal val = orcamento.getValorTotal();
		BigDecimal pg= pago;
		BigDecimal total = new BigDecimal(0);
		if(pg.compareTo(val) >=0){
		   total = pg.subtract(val);
		   troco= total.setScale(2);
		   pago=  pg.setScale(2);
		   //System.out.println(troco);
		   //System.out.println(pago);
		 }else{
			 troco=(new BigDecimal(0).setScale(2));
			 calcularTotal();
		 }
		 
		total=new BigDecimal(0);
		val=new BigDecimal(0);
		//troco=new BigDecimal(0);
		//pago=new BigDecimal(0); 
	}
	
	 
	public void cleanDescont(){
		descon = (new BigDecimal(1));
	}
	 
 
	public void salvar(){
		
		try{
			if(orcamento.getValorTotal().signum()==0){
				Messages.addGlobalWarn("Adicione ao menos um item a venda!");
				return;
			}
		 
			String formaPgto=forma;
		    System.out.println(formaPgto+"ooooooooooooo");
			OrcamentoDao orcaDao = new OrcamentoDao();
			orcamento.setSituacaoOrcamento(SituacaoOrcamento.OK);
			 
			orcaDao.salvar(orcamento, servicosOrcamento);
			 
			BigDecimal valorVenda=orcamento.getValorTotal();
			Date dia=orcamento.getData();
		    Caixa cai = orcamento.getCaixa();
		     
			lancamento.setCaixa(cai);
			lancamento.setData(dia);
			lancamento.setHistoricoLancamento("Venda Geral PDV");
			lancamento.setTipo("Entrada");
			lancamento.setValor(valorVenda);
			lancamento.setFormapgto(formaPgto);
			lancamento.setOrcamento(orcamento);
			LancamentosCaixaDao lanDao = new LancamentosCaixaDao();
			 
			lanDao.salvarLancamento(lancamento);
			Imp();
			
			mensagemOk();
			retornarClean();
			lancamento = new LancamentosCaixa();
			orcamento = new Orcamento();
			 
			troco=new BigDecimal(0);
			pago=new BigDecimal(0); 
		}catch(Exception e){
			Messages.addGlobalError("Ocorreu erro ao finalizar venda, verifique a quantidade em estoque");
			e.printStackTrace();
		}
	}
	 
	public String sairOrcamento(){
		lancamento = new LancamentosCaixa();
		orcamento = new Orcamento();
		troco=new BigDecimal(0);
		pago=new BigDecimal(0);
    	return "/pages/inicio.xhtml?faces-redirect=true";
    }
	
	 
	
	/*Metodo que evita o reenvio do form apos salvar caso se pressione f5 */
    public void  retornarClean() {
    	 
    	try {
    		 
			FacesContext.getCurrentInstance().getExternalContext().redirect("abriOrcamento.xhtml");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /*Metodo que evita o reenvio do form apos salvar caso se pressione f5 */
    public void  retornarCleanLista() {
    	 
    	try {
    		 
			FacesContext.getCurrentInstance().getExternalContext().redirect("listaOrcamentos.xhtml");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /*Metodo para permitir que as mensagens de confirmação seja apresentadas*/
    public String mensagemOk() {
    	  FacesContext.getCurrentInstance().addMessage(
    	        null, new FacesMessage("Venda Finalizada!"));
    	 
    	  FacesContext.getCurrentInstance()
    	      .getExternalContext()
    	      .getFlash().setKeepMessages(true);
    	 
    	  return "listar/abriOrcamento.xhtml?faces-redirect=true";
    	}
	
    /*Metodo para permitir que as mensagens de confirmação seja apresentadas*/
    public String mensagemAdicionarProduto() {
    	  FacesContext.getCurrentInstance().addMessage(
    	        null, new FacesMessage("Produto Adicionado!"));
    	 
    	  FacesContext.getCurrentInstance()
    	      .getExternalContext()
    	      .getFlash().setKeepMessages(true);
    	 
    	  return "listar/abriOrcamento.xhtml?faces-redirect=true";
    	}
    
  /*  public void editar(ActionEvent evento){
    	try{
    		orcamento=(Orcamento)evento.getComponent().getAttributes().get("orcamentoSelecionado");
    		cancelarOrcamento();
    	}catch(RuntimeException erros){
    		Messages.addGlobalError("Erro ao selecionar orçamento");
    	}
    } */
    public String mensagemCancelarOrcamento() {
  	  FacesContext.getCurrentInstance().addMessage(
  	        null, new FacesMessage("Orçamento Cancelado!"));
  	 
  	  FacesContext.getCurrentInstance()
  	      .getExternalContext()
  	      .getFlash().setKeepMessages(true);
  	 
  	  return "listar/abriOrcamento.xhtml?faces-redirect=true";
  	}
    public void cancelarOrcamento(){
    	try{
    		
    		orcamento.setSituacaoOrcamento(SituacaoOrcamento.CANCELADO);
    		orcamentoDao.merge(orcamento);
    		mensagemCancelarOrcamento(); 
    		retornarCleanLista();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void imprimirOrcamento(){
   	 
    	    HashMap parametros = new HashMap();
    		parametros.put("COD_ORCAMENTO",codigoOrcamento);
    		Connection conexao = HibernateUtil.getConexao();
    		UtilRelatorios.imprimirRelatorio("orcamento", parametros, conexao);
    		
    		 
    	 
    }
    
    public void changeRadio(ValueChangeEvent evento){
    	tipoReg = (Cliente)evento.getNewValue();
    }
 
    public void printOrcamento(ActionEvent evento){
    	try{
    		orcamento=(Orcamento)evento.getComponent().getAttributes().get("orcamentoSelecionado");
    		codigoOrcamento=orcamento.getCodigo();
    		 
    		 
    	}catch(RuntimeException erros){
    		Messages.addGlobalError("Erro ao selecionar cliente");
    	}
    }
    
    
    public void imprimir() {
    	Long nnn = numero=orcamentoDao.ultimoOrcamento(nroReg);
    	HashMap parametros = new HashMap();
		parametros.put("COD_ORCAMENTO",nnn);
		Connection conexao = HibernateUtil.getConexao();
		UtilRelatorios.imprimirRelatorio("orcamento", parametros, conexao);
		 
    }
    
    
    public void limpar() {
    	String nome = "/temporarios/OrcamentoTemp.pdf";  
        File f = new File(nome);  
        f.delete();
    }
 
public void imprimirLista(){
    	 
	HashMap parametros = new HashMap();
	//parametros.put("COD_ORCAMENTO",nnn);
	Connection conexao = HibernateUtil.getConexao();
	UtilRelatorios.imprimirRelatorio("orcamentosLista", parametros, conexao);
    	 
    }

public void cupomIreport() {
	 
		  
		 
}
    public void identificaOrcamento(){
    	numero=orcamentoDao.ultimoOrcamento(nroReg);
    	System.out.println(numero);
    }
    
   
    public void dadosRegistro() {
    	RegistroDao regDao = new RegistroDao();
    	registros=regDao.listar();
    	for(Registro r:registros) {
    		razao=r.getFantasia();
    		cpf=r.getCpfCnpj();
    		fone=r.getFone();
    		cel=r.getCelular();
    		rua=r.getRua();
    		nroRua=r.getNro();
    		
    	}
    }
    
    public int imprimirCup() {
    	dadosRegistro();
    	String s = null;
    	int n=7;
    	 
    	PrinterMatrix pri = new PrinterMatrix();
    	pri.setOutSize(1000, 80);
    	Extenso ext= new Extenso();
    	
    	pri.printCharAtLin(1, 1000, 1, s);
    	pri.printTextLinCol(2, 10, razao);
    	pri.printTextLinCol(3, 10, cpf);
    	pri.printTextLinCol(4, 1,"--------------------Sem Valor Fiscal------------------" );
    	pri.printTextLinCol(5, 1,"Nro Venda: "+String.valueOf(orcamento.getCodigo()));
    	pri.printTextLinCol(6, 1,"------------------------------------------------------" );
    	pri.printTextLinCol(7, 1,"Sub Total R$: " );
    	pri.printTextLinCol(7, 15,String.valueOf(lancamento.getValor()));
    	pri.printTextLinCol(8, 1,"Forma Pgto: " );
    	pri.printTextLinCol(8, 15,String.valueOf(lancamento.getFormapgto()));
    	pri.printTextLinCol(9, 1,"Valor Total R$: " );
    	pri.printTextLinCol(9, 19,String.valueOf(lancamento.getValor()));
    	pri.printTextLinCol(10, 1,"------------------------------------------------------" );
    	pri.printTextLinCol(11, 1,"Descricao" );  
    	pri.printTextLinCol(11, 28,"QTD" );
    	pri.printTextLinCol(11, 35,"Valor" );

    	int  linhas=13;
    	 
         for(ServicosOrcamento se:servicosOrcamento) {
     	    
     		   String des=se.getDescricao();
     		   String qt=String.valueOf(se.getQuantidade());
     		   String val=String.valueOf(se.getValor());
       
     		   pri.printTextLinCol(linhas, 1,des);
     		   pri.printTextLinCol(linhas, 28,qt); 
     		   NumberFormat itensVal = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));
     		   String valor=itensVal.format(new BigDecimal(val));
     		   pri.printTextLinCol(linhas, 35,valor); 
     	    linhas++;
         }
     		  
    	 
    	  pri.printCharAtCol(linhas, 1, 45, "-");
    	  pri.printCharAtCol(++linhas, 1, 40, "GRATO PELA PREFERÊNCIA! VOLTE SEMPRE");
    	  pri.printCharAtCol(linhas, 1, linhas, s);
    	/*pri.show();
    	pri.toFile("cupons.txt");
    	File arquivo = new File(System.getProperty("user.dir")+"C:");
    	if(arquivo.isFile()) {
    		arquivo.delete();
    	}*/
    	 
    	return n;
    }
    
    public int tamanho() {
    	int tama=imprimirCup();
    	return tama;
    }
    public int novoTam() {
    	int novo=tamanho();
    	return novo +4;
    }
    public int imprimirCupomReal() {
    	String dataF="dd/MM/yyyy";
    	String horaF= "H:mm - a";
    	String data, hora;
    	Date dia = new Date();
    	SimpleDateFormat formatada = new SimpleDateFormat(dataF);
    	data=formatada.format(dia);
    	formatada= new SimpleDateFormat(horaF);
    	hora=formatada.format(dia);
    	
    	dadosRegistro();
    	String s = null;
    	int n=7;
    	int tam=novoTam();
    	int ond=tam+24;
    	PrinterMatrix pri = new PrinterMatrix();
    	pri.setOutSize(ond+2, 48);
    	Extenso ext= new Extenso();
    	
    	pri.printCharAtCol(1, 1, 48, "=");
    	pri.printTextLinCol(2, 1,"Conferencia: " );
    	pri.printTextLinCol(3, 1,"Nro da Venda: "+String.valueOf(orcamento.getCodigo()));
      	pri.printTextLinCol(3, 20,data+"-"+hora); 
      	
      	pri.printTextLinCol(4, 1,"Forma Pgto: " );
      	pri.printTextLinCol(4, 15,String.valueOf(lancamento.getFormapgto()));
      	
      	pri.printTextLinCol(5, 1,"Caixa: " );
      	pri.printTextLinCol(5, 9,String.valueOf(lancamento.getCaixa().getDescricao()));
      	 
      	pri.printTextLinCol(7, 1,"Valor Pago: " );
      	String valorStringConfPago = String.valueOf(pago);
      	NumberFormat totalValorConfPago = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));
      	String valorTotalConfPago = totalValorConfPago.format(new BigDecimal(valorStringConfPago));
      	pri.printTextLinCol(7, 15,valorTotalConfPago ); 
      	
      	pri.printTextLinCol(8, 1,"Valor Troco: " );
      	String valorStringConfTroco = String.valueOf(troco);
      	NumberFormat totalValorConfTroco = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));
      	String valorTotalConfTroco = totalValorConfTroco.format(new BigDecimal(valorStringConfTroco));
      	pri.printTextLinCol(8, 15,valorTotalConfTroco ); 
      	
      	pri.printTextLinCol(9, 1,"Valor Total: " );
      	String valorStringConf = String.valueOf(lancamento.getValor());
      	NumberFormat totalValorConf = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));
      	String valorTotalConf = totalValorConf.format(new BigDecimal(valorStringConf));
      	pri.printTextLinCol(9, 15,valorTotalConf ); 
    	pri.printTextLinCol(10, 1,"Corte Aqui------------------------------------" );
    	pri.printCharAtCol(11, 1, 48, "=");
    	pri.printTextLinCol(12, 1, razao);
    	pri.printTextLinCol(13, 1, cpf);
    	pri.printTextLinCol(13, 20,"End:");
    	pri.printTextLinCol(13, 25, rua);
    	pri.printTextLinCol(13, 40, nroRua);
    	pri.printTextLinCol(14, 1, "Telefone:");
    	pri.printTextLinCol(14, 11, fone);
    	pri.printTextLinCol(14, 25, "Celular:");
    	pri.printTextLinCol(14, 34, cel);
    	pri.printTextLinCol(15, 1,"---------------Sem Valor Fiscal----------------" );
    	pri.printTextLinCol(16, 1,"Nro Venda: "+String.valueOf(orcamento.getCodigo()));
    	pri.printTextLinCol(16, 20,data+"-"+hora);
    	pri.printTextLinCol(17, 1,"-----------------------------------------------" );
    	 
    	 pri.printTextLinCol(18, 1,"Sub Total: " );
         String sub = String.valueOf(lancamento.getValor());
         NumberFormat sublValor = NumberFormat.getCurrencyInstance(new Locale("pt","BR")); 
         String subTotal = sublValor.format(new BigDecimal(sub));
      	pri.printTextLinCol(18, 15,subTotal);
      	
      	pri.printTextLinCol(19, 1,"Forma Pgto: " );
      	pri.printTextLinCol(19, 15,String.valueOf(lancamento.getFormapgto()));
      	
      	pri.printTextLinCol(20, 1,"Valor Total: " );
      	String valorString = String.valueOf(lancamento.getValor());
      	NumberFormat totalValor = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));
      	String valorTotal = totalValor.format(new BigDecimal(valorString));
      	pri.printTextLinCol(20, 15,valorTotal ); 
      	pri.printTextLinCol(21, 1,"-----------------------------------------------" );
      	pri.printTextLinCol(22, 1,"Descricao" );  
    	pri.printTextLinCol(22, 34,"QTD" );
    	pri.printTextLinCol(22, 39,"Valor" );
    	pri.printTextLinCol(23, 1,"-----------------------------------------------" );
    	
    	
    	int  linhas=24;
    	 
         for(ServicosOrcamento se:servicosOrcamento) {
     	    
     		   String des=se.getDescricao();
     		   String qt=String.valueOf(se.getQuantidade());
     		   String val=String.valueOf(se.getValor());
       
     		   pri.printTextLinCol(linhas, 1,des);
     		   pri.printTextLinCol(linhas, 34,qt); 
     		   NumberFormat itensVal = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));
     		   String valor=itensVal.format(new BigDecimal(val));
     		   pri.printTextLinCol(linhas, 39,valor); 
     	    linhas++;
     	    
         }
        pri.printCharAtCol(linhas, 1, 48, "-");
        pri.printTextLinCol(++linhas, 1,"GRATO PELA PREFERENCIA, VOLTE SEMPRE!" );
        pri.printCharAtCol(linhas, 1, 48, "-");
        pri.printCharAtCol(++linhas, 1, 48, ",");
        pri.show();
    	pri.toFile("cupons.txt");
    	/* File arquivo = new File(System.getProperty("user.dir")+"\\cupons.txt");
    	if(arquivo.isFile()) {
    		arquivo.delete();
    	}*/
    	return n;
    }
    
    public void Imp() {
    	 
    	  imprimirCupomReal();
          gerarCupons();
    }
    
    public int gerarCupons(){
        
        PrintService[] printService=PrintServiceLookup.lookupPrintServices(DocFlavor.INPUT_STREAM.AUTOSENSE,null);
        System.out.println("Quantas Impressoras.: "+printService.length);
        PrintService impressoraPadrao = PrintServiceLookup.lookupDefaultPrintService();
        System.out.println("A impressora Padrão é a "+impressoraPadrao.getName());
        DocFlavor docFlavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        HashDocAttributeSet hasDocAttributeSet =  new HashDocAttributeSet();
        try {
         
            //FileInputStream fileInputStream = new FileInputStream(System.getProperty("user.dir")+"\\cupons.txt");
            FileInputStream fileInputStream = new FileInputStream("cupons.txt");
            Doc doc = new SimpleDoc(fileInputStream,docFlavor,hasDocAttributeSet);
            PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
            PrintService printServico = ServiceUI.printDialog(null, 200, 300, printService, impressoraPadrao, docFlavor, printRequestAttributeSet);
                     
                      if(printServico !=null){
                          DocPrintJob docPrintJob = printServico.createPrintJob();
                try {
                    docPrintJob.print(doc, printRequestAttributeSet);
                    
                } catch (PrintException ex) {
                    JOptionPane.showMessageDialog(null, ex);
                     ex.printStackTrace();
                }
                      }

        } catch (FileNotFoundException ex) {
             ex.printStackTrace();
        }
                    // TODO add your handling code here:
         
        
        
        return 0;
        
        
    }
	public List<Produto> getProdutos() {
		return produtos;
	}
	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}
	 
	public List<ServicosOrcamento> getServicosOrcamento() {
		return servicosOrcamento;
	}
	public void setServicosOrcamento(List<ServicosOrcamento> servicosOrcamento) {
		this.servicosOrcamento = servicosOrcamento;
	}

	public List<ServicosOrcamento> getProdutosOrcamento() {
		return produtosOrcamento;
	}

	public void setProdutosOrcamento(List<ServicosOrcamento> produtosOrcamento) {
		this.produtosOrcamento = produtosOrcamento;
	}

	 

	public Orcamento getOrcamento() {
		return orcamento;
	}
    
	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}



	public List<Cliente> getClientes() {
		return clientes;
	}



	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}



	public Cliente getCliente() {
		return cliente;
	}



	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}

	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public PieChartModel getPizza() {
		return pizza;
	}

	public void setPizza(PieChartModel pizza) {
		this.pizza = pizza;
	}

	public List<Orcamento> getOrcamentos() {
		return orcamentos;
	}

	public void setOrcamentos(List<Orcamento> orcamentos) {
		this.orcamentos = orcamentos;
	}

	public OrcamentoDao getOrcamentoDao() {
		return orcamentoDao;
	}

	public void setOrcamentoDao(OrcamentoDao orcamentoDao) {
		this.orcamentoDao = orcamentoDao;
	}

	public Long getTotalG() {
		return totalG;
	}

	public void setTotalG(Long totalG) {
		this.totalG = totalG;
	}

	public ServicosOrcamentosDao getServorcaDao() {
		return servorcaDao;
	}

	public void setServorcaDao(ServicosOrcamentosDao servorcaDao) {
		this.servorcaDao = servorcaDao;
	}

	public List<ServicosOrcamento> getServicosOrcamentoPizza() {
		return servicosOrcamentoPizza;
	}

	public void setServicosOrcamentoPizza(
			List<ServicosOrcamento> servicosOrcamentoPizza) {
		this.servicosOrcamentoPizza = servicosOrcamentoPizza;
	}

	public Long getProdutoOrca() {
		return produtoOrca;
	}

	public void setProdutoOrca(Long produtoOrca) {
		this.produtoOrca = produtoOrca;
	}
 
	public SituacaoOrcamento [] getSituacaoOrcamentos(){
		return SituacaoOrcamento.values();
	}

	public BigDecimal getDescon() {
		return descon;
	}

	public void setDescon(BigDecimal descon) {
		this.descon = descon;
	}

	public List<String> getLista() {
		return lista;
	}

	public void setLista(List<String> lista) {
		this.lista = lista;
	}
 

	public Long getNroReg() {
		return nroReg;
	}

	public void setNroReg(Long nroReg) {
		this.nroReg = nroReg;
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public Cliente getTipoReg() {
		return tipoReg;
	}

	public void setTipoReg(Cliente tipoReg) {
		this.tipoReg = tipoReg;
	}

	public FuncionarioDao getFuncionarioDao() {
		return funcionarioDao;
	}

	public void setFuncionarioDao(FuncionarioDao funcionarioDao) {
		this.funcionarioDao = funcionarioDao;
	}

	public Long getCodigoOrcamento() {
		return codigoOrcamento;
	}

	public void setCodigoOrcamento(Long codigoOrcamento) {
		this.codigoOrcamento = codigoOrcamento;
	}

	public List<Orcamento> getListadeOrcamentos() {
		return listadeOrcamentos;
	}

	public void setListadeOrcamentos(List<Orcamento> listadeOrcamentos) {
		this.listadeOrcamentos = listadeOrcamentos;
	}

	public ServicosOrcamento getServico() {
		return servico;
	}

	public void setServico(ServicosOrcamento servico) {
		this.servico = servico;
	}

	public Produto getProdutoB() {
		return produtoB;
	}

	public void setProdutoB(Produto produtoB) {
		this.produtoB = produtoB;
	}

	public BigDecimal getTroco() {
		return troco;
	}

	public void setTroco(BigDecimal troco) {
		this.troco = troco;
	}

	public BigDecimal getPago() {
		return pago;
	}

	public void setPago(BigDecimal pago) {
		this.pago = pago;
	}

	public LancamentosCaixa getLancamento() {
		return lancamento;
	}

	public void setLancamento(LancamentosCaixa lancamento) {
		this.lancamento = lancamento;
	}

	public String getForma() {
		return forma;
	}

	public void setForma(String forma) {
		this.forma = forma;
	}

	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}

	public Map<String, String> getFormas() {
		return formas;
	}

	public void setFormas(Map<String, String> formas) {
		this.formas = formas;
	}

	public String getRazao() {
		return razao;
	}

	public void setRazao(String razao) {
		this.razao = razao;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public AutenticacaoBean getAutentica() {
		return autentica;
	}

	public void setAutentica(AutenticacaoBean autentica) {
		this.autentica = autentica;
	}
    
	 
	 
	
	
}
