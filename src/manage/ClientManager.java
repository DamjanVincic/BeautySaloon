package manage;

import entity.Client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ClientManager {
    private String clientFile;
    private ArrayList<Client> clients;

    public ClientManager(String clientFile) {
        this.clientFile = clientFile;
        this.clients = new ArrayList<>();
    }

    public Client findClientByUsername(String username) {
        Client client;
        try {
            ArrayList<Client> filtered = new ArrayList<Client>(this.clients.stream()
                                                                .filter(c -> c.getUsername().equals(username))
                                                                .collect(Collectors.toList()));
            client = filtered.get(0);
        } catch (IndexOutOfBoundsException ex) {
            client = null;
        }
        return client;
    }

    public boolean loadData() {
        try {
			BufferedReader br = new BufferedReader(new FileReader(this.clientFile));
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] data = line.split(",");
                Client client = new Client(data[0], data[1], data[2], data[3], data[4], data[5], data[6]);
				this.clients.add(client);
			}
			br.close();
		} catch (IOException e) {
            System.out.println("Error with saving data.");
			return false;
		}
		return true;
    }

    public boolean saveData() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(this.clientFile, false));
            for (Client client : this.clients) {
                pw.println(client.toFileString());
            }
			pw.close();
		} catch (IOException e) {
            System.out.println("Error with loading data.");
			return false;
		}
		return true;
	}

    public void add(String name, String surname, String gender, String phone, String address, String username, String password) {
        if (this.findClientByUsername(username) != null) {
            System.out.println("Client with given username already exists.");
            return;
        }
        
        this.clients.add(new Client(name, surname, gender, phone, address, username, password));
        if (!this.saveData()) {
            return;
        }
        
        System.out.println("Client successfully added.");
    }

    public void update(String name, String surname, String gender, String phone, String address, String username, String password) {
		Client client = this.findClientByUsername(username);
        if (client == null) {
            System.out.println("Client with given username does not exist.");
            return;
        }
        client.setName(name);
        client.setSurname(surname);
        client.setGender(gender);
        client.setPhone(phone);
        client.setAddress(address);
        client.setUsername(username);
        client.setPassword(password);

		if (!this.saveData()) {
            return;
        }

        System.out.println("Client successfully edited.");
	}

	public void remove(String username) {
        Client client = this.findClientByUsername(username);
        if (client == null) {
            System.out.println("Client does not exist.");
            return;
        }
        this.clients.remove(client);
        if (!this.saveData()) {
            return;
        }
        System.out.println("Client successfully deleted.");
	}
}
