package br.com.caelum.stella.boleto.bancos;

import java.io.IOException;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.stella.boleto.Boleto;
import br.com.caelum.stella.boleto.Datas;
import br.com.caelum.stella.boleto.Emissor;
import br.com.caelum.stella.boleto.Sacado;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HSBCTest {
	
	private Boleto boleto;
	private HSBC banco = new HSBC();

	@Before
	public void setUp() {

		Datas datas = Datas.novasDatas().comDocumento(28,1,2013)
				.comProcessamento(29,1,2013).comVencimento(30,1,2013);

		Emissor emissor = Emissor.novoEmissor().comCedente("Rodrigo Turini")
			.comCodigoFornecidoPelaAgencia("4146239").comNossoNumero("1476147");

	    Sacado sacado = Sacado.novoSacado().comNome("Paulo Silveira");
		
	    this.boleto = Boleto.novoBoleto().comDatas(datas).comEmissor(emissor)
	    	.comBanco(this.banco).comSacado(sacado).comValorBoleto(3383.00)
	    	.comNumeroDoDocumento("0789201");
	}

	@Test
	public void testLinhaDoBancoHSBC() {
		String codigoDeBarras = boleto.getBanco().geraCodigoDeBarrasPara(this.boleto);
		assertEquals("39994.14620  39000.000149  76147.030324  5  55940000338300",
			new GeradorDeLinhaDigitavel().geraLinhaDigitavelPara(codigoDeBarras,this.banco));
	}

	@Test
	public void testDataJuliana() {
		Calendar vencimento = boleto.getDatas().getVencimento();
		assertEquals("0303", banco.getDataFormatoJuliano(vencimento, 4));
	}

	@Test
	public void testCodigoDeBarraDoBancoHSBC() {
		this.boleto = this.boleto.comBanco(this.banco);
		String codigoDeBarras = this.banco.geraCodigoDeBarrasPara(this.boleto);
		assertEquals("39995559400003383004146239000000147614703032", codigoDeBarras);
	}
	
	@Test
	public void testDigitosNossoNumeroHSBC(){
		this.boleto = this.boleto.comBanco(this.banco);
		String nossoNumeroCompleto = this.banco.getNossoNumeroECodDocumento(boleto);
		assertEquals("0000001476147541", nossoNumeroCompleto);
	}
	
	@Test
	public void testDigitosNossoNumeroHSBCComDadosDoManual(){
		this.boleto = this.boleto.comBanco(this.banco);
		
		this.boleto.getEmissor().comNossoNumero(239104761);
		this.boleto.getEmissor().comCodigoFornecidoPelaAgencia(8351202);
		this.boleto.getDatas().comVencimento(4, 7, 2008);
		
		String nossoNumeroCompleto = this.banco.getNossoNumeroECodDocumento(boleto);
		assertEquals("0000239104761941", nossoNumeroCompleto);
	}

	@Test
	public void testGetImage() throws IOException {
		assertNotNull(this.banco.getImage());
	}
}