package manage;

import entity.Client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ClientManager {
    private String clientFile;
    private ArrayList<Client> clients;

    public ClientManager(String clientFile) {
        this.clientFile = clientFile;
        this.clients = new ArrayList<>();
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
			return false;
		}
		return true;
    }

    public boolean saveData() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(this.clientFile, false));
            for (Client client : clients) {
                pw.println(client.toFileString());
            }
			pw.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

    public void add(String name, String surname, String gender, String phone, String address, String username, String password) {
        this.clients.add(new Client(name, surname, gender, phone, address, username, password));
        this.saveData();
    }
}
