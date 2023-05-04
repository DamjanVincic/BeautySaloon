package manage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.stream.Stream;

import entity.PriceList;
import entity.Service;

public class PriceListManager {
    private String priceFile;
    // private HashMap<TreatmentType, Double> prices;
    private HashMap<Integer, PriceList> priceLists;
    private ServiceManager serviceManager;

    public PriceListManager(String priceFile, ServiceManager serviceManager) {
        this.priceFile = priceFile;
        this.priceLists = new HashMap<>();
        this.serviceManager = serviceManager;
    }

    public PriceList findPriceListByID(int id) {
        return this.priceLists.get(id);
    }

    public boolean loadData() {
        try {
			BufferedReader br = new BufferedReader(new FileReader(this.priceFile));
			String line = null;
			while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                // HashMap<TreatmentType, Double> prices = Stream.of(data[1].split("|")).collect(Collectors.toMap(e -> this.treatmentTypeManager.findTreatmentByID(Integer.parseInt(e.split(";")[1])), e -> Double.parseDouble(e.split(";")[1])));
                HashMap<Service, Double> prices = new HashMap<>();
                try {
                    Stream.of(data[1].split("\\|")).forEach(e -> prices.put(this.serviceManager.findServiceByID(Integer.parseInt(e.split(";")[0])), Double.parseDouble(e.split(";")[1])));
                } catch (ArrayIndexOutOfBoundsException ex) { }
                PriceList priceList = new PriceList(Integer.parseInt(data[0]));
                priceList.setPrices(prices);
                this.priceLists.put(Integer.parseInt(data[0]), priceList);
            }
			br.close();
            if (!this.priceLists.isEmpty()) {
                PriceList.setCount(this.priceLists.values().stream().map(PriceList::getId).max(Integer::compare).get());
            }
		} catch (IOException e) {
			return false;
		}
		return true;
    }

    public boolean saveData() {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(this.priceFile));
            this.priceLists.values().forEach(e -> pw.println(e.toFileString()));
			pw.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

    public void add() throws Exception {
        PriceList priceList = new PriceList();
        this.priceLists.put(priceList.getId(), priceList);
        this.saveData();
    }

    public void add(int priceListID, int serviceID, double price) throws Exception {
        // if (priceListID < 1) id mora biti veci od 0
        PriceList priceList = this.findPriceListByID(priceListID);
        if (priceList == null) {
            priceList = new PriceList(priceListID);
        }

        Service service = this.serviceManager.findServiceByID(serviceID);
        if (service == null) {
            throw new Exception("Service does not exist.");
        }
        if (priceList.getPrice(service) != null) {
            throw new Exception("Service already has a price set.");
        }

        priceList.setPrice(service, price);
        this.priceLists.put(priceListID, priceList);
        this.saveData();
    }

    public void update(int priceListID, int serviceID, double price) throws Exception {
        PriceList priceList = this.findPriceListByID(priceListID);
        if (priceList == null) {
            throw new Exception("Price list does not exist.");
        }

        Service service = this.serviceManager.findServiceByID(serviceID);
        if (service == null) {
            throw new Exception("Service does not exist.");
        }
        if (priceList.getPrice(service) == null) {
            throw new Exception("Service does not have a price set.");
        }

        priceList.setPrice(service, price);
        this.priceLists.put(priceListID, priceList);
        this.saveData();
	}

    public void remove(int priceListID) throws Exception {
        if (this.findPriceListByID(priceListID) == null) {
            throw new Exception("Price list does not exist.");
        }
        this.priceLists.remove(priceListID);
        this.saveData();
    }

	public void remove(int priceListID, int serviceID) throws Exception {
        PriceList priceList = this.findPriceListByID(priceListID);
        if (priceList == null) {
            throw new Exception("Price list does not exist.");
        }

        Service service = this.serviceManager.findServiceByID(serviceID);
        if (service == null) {
            throw new Exception("Service does not exist.");
        }
        if (priceList.getPrice(service) == null) {
            throw new Exception("Service does not have a price set.");
        }
        priceList.remove(service);
        // this.priceLists.put(priceListID, priceList);
        this.saveData();
	}
}
