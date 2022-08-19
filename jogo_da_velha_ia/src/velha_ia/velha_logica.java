package velha_ia;

public class velha_logica {

	// Clase onde será desenvolvida a lógica do jogo da velha

	private char id;

	private String situacao;

	private char[] tabuleiro = new char[9];

	// informar o cosntrutor da classe

	// Condicao para limpar todos os campos deste tabuleiro

	public void limpar() {

		this.id = velha_logica2.vazio;
		situacao = velha_logica2.status_aguarde;
		for (int pos = 0; pos < tabuleiro.length; pos++) {
			tabuleiro[pos] = velha_logica2.vazio;
		}
	}

	public char getPosicao(final int pos) {
		return tabuleiro[pos];
	}

	public void setPosicao(final int pos, final char jogador) {
		tabuleiro[pos] = jogador;
	}

	public char getId() {
		return id;
	}

	public void setId(char id) {
		this.id = id;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(final String status) {
		this.situacao = status;
	}
	

}
