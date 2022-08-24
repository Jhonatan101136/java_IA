package game.ia;



public class velha_classe_ia {

	private char id;

	private char[] tabuleiro = new char[9];

	private String situacao;

	public velha_classe_ia() {
		limpar();
	}

	public void limpar() {
		this.id = Velha_IA.vazio;
		situacao = Velha_IA.status_aguarde;
		for (int pos = 0; pos < tabuleiro.length; pos++) {
			tabuleiro[pos] = Velha_IA.vazio;
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

	public void setId(final char id) {
		this.id = id;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(final String status) {
		this.situacao = status;
	}

}
