import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class TodoList extends JFrame {
    // atributos
    private JPanel mainPanel;
    private JTextField taskInputField;
    private JButton addButton;
    private JList<String> taskList;
    private DefaultListModel<String> listModel;
    private JButton deleteButton;
    private JButton markDoneButton;
    private JComboBox<String> filterComboBox;
    private JButton clearCompletedButton;

    private List<Task> tasks;

    public TodoList() {
        // Configuração da janela principal
        super("To-Do List App");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(450, 400);
        this.setVisible(true);

        // Inicializa o painel principal
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Inicializa a lista de tasks e a lista de tasks concluídas
        tasks = new ArrayList<>();
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);

        // Inicializa campos de entrada, botões e JComboBox
        taskInputField = new JTextField();
        addButton = new JButton("Adicionar");
        deleteButton = new JButton("Excluir");
        markDoneButton = new JButton("Concluir");
        filterComboBox = new JComboBox<>(new String[] { "Todas", "Ativas", "Concluídas" });
        clearCompletedButton = new JButton("Limpar Concluídas");
        //estiliza o botao
        //botao de adicionar
        addButton.setBackground(Color.BLUE);
        addButton.setForeground(Color.WHITE);
        //botao de excluir
        deleteButton.setBackground(Color.RED);
        deleteButton.setForeground(Color.WHITE);
        //botao de concluir
        markDoneButton.setBackground(Color.GREEN);
        markDoneButton.setForeground(Color.WHITE);
        //botao de atividades - Todas - Ativas - Concluidas
        filterComboBox.setBackground(Color.gray);
        filterComboBox.setForeground(Color.WHITE);
        //botao de limpar concluidas
        clearCompletedButton.setBackground(Color.magenta);
        clearCompletedButton.setForeground(Color.WHITE);

        // Configuração do painel de entrada
        JPanel painel = new JPanel(new BorderLayout());
        painel.add(taskInputField, BorderLayout.CENTER);
        painel.add(addButton, BorderLayout.EAST);

        // Configuração do painel de botões
        JPanel botao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botao.add(deleteButton);
        botao.add(markDoneButton);
        botao.add(filterComboBox);
        botao.add(clearCompletedButton);

        // Adiciona os componentes ao painel principal
        mainPanel.add(painel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(taskList), BorderLayout.CENTER);
        mainPanel.add(botao, BorderLayout.SOUTH);
        // Adiciona o painel principal à janela
        this.add(mainPanel);

        // Adiciona tratamento de eventos aos componentes
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addTask();
                JOptionPane.showMessageDialog(null,"A tarefa foi adicionada com sucesso");
            }
        });
        //botao de deleta tarefa
deleteButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            int option = JOptionPane.showConfirmDialog(null, "Você deseja excluir esta tarefa?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                tasks.remove(selectedIndex);
                updateTaskList();
                JOptionPane.showMessageDialog(null, "Tarefa excluída com sucesso!");
            } else if (option == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog(null, "Exclusão cancelada.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Selecione uma tarefa para excluir.");
        }
    }
});
// botao que marc atarefa como concluida
markDoneButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        int selectedIndex = taskList.getSelectedIndex();
        if (selectedIndex != -1) {
            int option = JOptionPane.showConfirmDialog(null, "Você deseja marcar esta tarefa como concluída?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                Task selectedTask = tasks.get(selectedIndex);
                selectedTask.setDone(true);
                updateTaskList();
                JOptionPane.showMessageDialog(null, "Tarefa marcada como concluída!");
            } else if (option == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog(null, "Operação cancelada.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Selecione uma tarefa para marcar como concluída.");
        }
    }
});

    // tratamento que transfere as tarefas para onde tem que ir (todas), (ativas) e (concluidas)
filterComboBox.addItemListener(new ItemListener() {
    public void itemStateChanged(ItemEvent e) {
        String selectedFilter = (String) filterComboBox.getSelectedItem();

        // Filtra as tarefas com base na seleção do JcomboBox
        switch (selectedFilter) {
            case "Todas":
                updateTaskList(tasks);
                break;
            case "Ativas":
                List<Task> tasksAtivas = new ArrayList<>();
                for (Task task : tasks) {
                    if (!task.isDone()) {
                        tasksAtivas.add(task);
                    }
                }
                updateTaskList(tasksAtivas);
                break;
            case "Concluídas":
                List<Task> tasksConcluidas = new ArrayList<>();
                for (Task task : tasks) {
                    if (task.isDone()) {
                        tasksConcluidas.add(task);
                    }
                }
                updateTaskList(tasksConcluidas);
                break;
        }
    }
    private void updateTaskList(List<Task> tasksToShow) {
        listModel.clear();
        for (Task task : tasksToShow) {
            listModel.addElement(task.getDescription());
        }
    }
});

clearCompletedButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        int option = JOptionPane.showConfirmDialog(null, "Você deseja limpar as tarefas concluídas?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            // Itera sobre a lista de tarefas e remove as tarefas concluídas
            List<Task> activeTasks = new ArrayList<>();
            for (Task task : tasks) {
                if (!task.isDone()) {
                    activeTasks.add(task);
                }
            }
            
            // Atualiza a lista de tarefas com as tarefas ativas (não concluídas)
            tasks = activeTasks;
            
            // Atualiza a lista de tarefas na interface gráfica
            updateTaskList(tasks);
            
            JOptionPane.showMessageDialog(null, "Tarefas concluídas removidas com sucesso!");
        } else if (option == JOptionPane.NO_OPTION) {
            JOptionPane.showMessageDialog(null, "Remoção de tarefas concluídas cancelada.");
        }
    }

    private void updateTaskList(List<Task> tasksToShow) {
        listModel.clear();
        for (Task task : tasksToShow) {
            listModel.addElement(task.getDescription());
        }
    }
});
    //tratamento que arrum todos os comandos de mouse na tela
    taskList.addMouseListener(new MouseAdapter() {
    public void mouseClicked(MouseEvent e) {
        // Verifica se o evento é um clique duplo
        if (e.getClickCount() == 2) {
            // Obtém o índice do item clicado na lista de tarefas
            int selectedIndex = taskList.getSelectedIndex();
            
            // Verifica se algum item está selecionado
            if (selectedIndex != -1) {
                // Lógica para tratar um clique duplo na tarefa
                Task selectedTask = tasks.get(selectedIndex);
                // Por exemplo, você pode exibir uma mensagem com a descrição da tarefa
                JOptionPane.showMessageDialog(null, "Tarefa: " + selectedTask.getDescription());
            }
        }
    }
});

//tratamento que arruma todos os comandos de teclado na tela
taskInputField.addKeyListener(new KeyAdapter() {
    public void keyPressed(KeyEvent e) {
        // Verifica se a tecla Enter foi pressionada
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            addTask(); // Adiciona a tarefa quando a tecla Enter é pressionada
        }
    }
});

//tratamento que arruma todos os problemas de foco da tela
taskInputField.addFocusListener(new FocusAdapter() {
    public void focusGained(FocusEvent e) {
        // Lógica para tratar quando o campo de entrada ganha foco
        // Por exemplo, você pode limpar o texto do campo quando ele ganha foco
        taskInputField.setText("digite");
    }

    public void focusLost(FocusEvent e) {
        // Lógica para tratar quando o campo de entrada perde o foco
        // Por exemplo, você pode validar o texto do campo quando ele perde foco
        String taskDescription = taskInputField.getText().trim();
        if (taskDescription.isEmpty()) {
            // Se o campo estiver vazio, você pode exibir uma mensagem de erro ou definir um valor padrão
            taskInputField.setText("Digite uma tarefa...");
        }
    }
});
    }
    
    // Métodos adicionais
    private void addTask() {
        // Adiciona uma nova task à lista de tasks
        String taskDescription = taskInputField.getText().trim();
        if (!taskDescription.isEmpty()) {
            Task newTask = new Task(taskDescription);
            tasks.add(newTask);
            updateTaskList();
            taskInputField.setText("");
        }
    }

    private void updateTaskList() {
        // Atualiza a lista de tarefas na interface gráfica
        listModel.clear();
        for (Task task : tasks) {
            listModel.addElement(task.getDescription());
        }
    }
}