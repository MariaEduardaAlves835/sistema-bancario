package br.com.sistemabancario.util;

public final class Validador {

    private static final int TAMANHO_CPF = 11;

    private Validador() {
    }

    public static String somenteDigitos(String texto) {
        return texto == null ? "" : texto.replaceAll("\\D", "");
    }

    public static boolean cpfValido(String cpf) {
        String digitos = somenteDigitos(cpf);
        if (digitos.length() != TAMANHO_CPF || todosIguais(digitos)) {
            return false;
        }
        return digitos.charAt(9) == calcularDigito(digitos, 9)
                && digitos.charAt(10) == calcularDigito(digitos, 10);
    }

    private static boolean todosIguais(String digitos) {
        return digitos.chars().distinct().count() == 1;
    }

    private static char calcularDigito(String digitos, int quantidade) {
        int soma = 0;
        for (int i = 0; i < quantidade; i++) {
            soma += Character.getNumericValue(digitos.charAt(i)) * (quantidade + 1 - i);
        }
        int resto = (soma * 10) % 11;
        return (char) ((resto == 10 ? 0 : resto) + '0');
    }
}