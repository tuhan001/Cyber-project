package bruteforceattack;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BruteForceAttackSimulator {

    private JFrame frame;
    private JTextArea inputTextArea;
    private JComboBox<String> cipherComboBox;
    private JTextArea outputTextArea;

    public BruteForceAttackSimulator() {
        // Set up the GUI components
        frame = new JFrame("Brute Force Attack Simulator on Substitution Ciphers");
        inputTextArea = new JTextArea(5, 30);
        cipherComboBox = new JComboBox<>(new String[]{"Additive", "Multiplicative", "Affine"});
        JButton attackButton = new JButton("Attack");
        outputTextArea = new JTextArea(15, 30);
        outputTextArea.setEditable(false);

        // Layout components
        JPanel panel = new JPanel();
        panel.add(new JLabel("Encrypted Text:"));
        panel.add(new JScrollPane(inputTextArea));
        panel.add(new JLabel("Cipher Type:"));
        panel.add(cipherComboBox);
        panel.add(attackButton);

        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.NORTH);
        frame.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        attackButton.addActionListener(e -> performAttack());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void performAttack() {
        String encryptedText = inputTextArea.getText().toUpperCase();
        String cipherType = (String) cipherComboBox.getSelectedItem();
        outputTextArea.setText("");

        List<String> results = new ArrayList<>();
        switch (cipherType) {
            case "Additive":
                results = bruteForceAdditiveCipher(encryptedText);
                break;
            case "Multiplicative":
                results = bruteForceMultiplicativeCipher(encryptedText);
                break;
            case "Affine":
                results = bruteForceAffineCipher(encryptedText);
                break;
        }

        // Display all results
        for (String result : results) {
            outputTextArea.append(result + "\n");
        }
    }

    private List<String> bruteForceAdditiveCipher(String encryptedText) {
        List<String> results = new ArrayList<>();
        for (int key = 1; key < 26; key++) {
            StringBuilder decrypted = new StringBuilder();
            for (char ch : encryptedText.toCharArray()) {
                if (Character.isLetter(ch)) {
                    char decryptedChar = (char) ((ch - 'A' - key + 26) % 26 + 'A');
                    decrypted.append(decryptedChar);
                } else {
                    decrypted.append(ch);
                }
            }
            results.add("Key " + key + ": " + decrypted.toString());
        }
        return results;
    }

    private List<String> bruteForceMultiplicativeCipher(String encryptedText) {
        List<String> results = new ArrayList<>();
        for (int key = 1; key < 26; key++) {
            if (gcd(key, 26) == 1) {  // Only use keys coprime with 26
                StringBuilder decrypted = new StringBuilder();
                int inverseKey = modInverse(key, 26);
                for (char ch : encryptedText.toCharArray()) {
                    if (Character.isLetter(ch)) {
                        char decryptedChar = (char) ((inverseKey * (ch - 'A') % 26 + 26) % 26 + 'A');
                        decrypted.append(decryptedChar);
                    } else {
                        decrypted.append(ch);
                    }
                }
                results.add("Key " + key + ": " + decrypted.toString());
            }
        }
        return results;
    }

    private List<String> bruteForceAffineCipher(String encryptedText) {
        List<String> results = new ArrayList<>();
        for (int a = 1; a < 26; a++) {
            if (gcd(a, 26) == 1) {  // Only use 'a' values coprime with 26
                int inverseA = modInverse(a, 26);
                for (int b = 0; b < 26; b++) {
                    StringBuilder decrypted = new StringBuilder();
                    for (char ch : encryptedText.toCharArray()) {
                        if (Character.isLetter(ch)) {
                            char decryptedChar = (char) (((inverseA * ((ch - 'A' - b + 26)) % 26 + 26) % 26) + 'A');
                            decrypted.append(decryptedChar);
                        } else {
                            decrypted.append(ch);
                        }
                    }
                    results.add("a = " + a + ", b = " + b + ": " + decrypted.toString());
                }
            }
        }
        return results;
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return 1;  // Should never reach here if a is coprime with m
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BruteForceAttackSimulator::new);
    }
}
