package br.udesc.ceavi.alg.pipes;

public enum Orientacao {

    NORTE(1), LESTE(2), SUL(3), OESTE(4);

    private int or;

    Orientacao(int orientacao) {
        this.or = orientacao;
    }

    public int getOrientacaoNum() {
        return this.or;
    }

    public static Orientacao fromNum(int num) {
        for (Orientacao value : Orientacao.values()) {
            if (value.getOrientacaoNum() == num) {
                return value;
            }
        }
        throw new java.lang.IllegalArgumentException("Orientacao " + num + " não encontrada.");
    }

    @Override
    public String toString() {
        return String.valueOf(this.or);
    }

}
