package notebook.model.repository.impl;

import notebook.model.User;
import notebook.model.repository.GBRepository;
import notebook.util.mapper.impl.UserMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserRepository implements GBRepository {
    private final UserMapper mapper;
    private final String fileName;

    public UserRepository(String fileName) {
        this.mapper = new UserMapper();
        this.fileName = fileName;
    }

    @Override
    public List<User> findAll() {
        List<String> lines = readAll();
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            users.add(mapper.toOutput(line));
        }
        return users;
    }

    @Override
    public User create(User user) {
        List<User> users = findAll();
        long max = 0L;
        for (User u : users) {
            long id = u.getId();
            if (max < id) {
                max = id;
            }
        }
        long next = max + 1;
        user.setId(next);
        users.add(user);
        write(users);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> update(Long userId, User update) {
        List<User> users = findAll();
        User editUser = users.stream()
                .filter(u -> u.getId()
                        .equals(userId))
                .findFirst().orElseThrow(() -> new RuntimeException("User not found"));
        if (!update.getFirstName().isEmpty()) {
            editUser.setFirstName(update.getFirstName());
        }
        if (!update.getLastName().isEmpty()) {
            editUser.setLastName(update.getLastName());
        }
        if (!update.getPhone().isEmpty()) {
            editUser.setPhone(update.getPhone());
        }
        write(users);
        return Optional.of(update);
    }

    @Override
    public boolean delete(Long id) {
        List<User> users = findAll();
        users.removeIf(u -> Objects.equals(u.getId(), id));
        write(users);
        return true;
    }

    private List<String> readAll() {
        List<String> lines = new ArrayList<>();
        try {
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            if (line != null) {
                lines.add(line);
            }
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    lines.add(line);
                }
            }
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    private void write(List<User> users) {
        List<String> lines = new ArrayList<>();
        for (User u : users) {
            lines.add(mapper.toInput(u));
        }
        saveAll(lines);
    }

    private void saveAll(List<String> data) {
        try (FileWriter writer = new FileWriter(fileName, false)) {
            for (String line : data) {
                writer.write(line);
                writer.append('\n');
            }
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
