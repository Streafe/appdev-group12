package no.ntnu.gr12.krrr_project.services;

import no.ntnu.gr12.krrr_project.models.Helmet;
import no.ntnu.gr12.krrr_project.models.Item;
import no.ntnu.gr12.krrr_project.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService {
        @Autowired
        ItemRepository itemRepository;

        @Transactional
        public String addItem(Item item) {
            itemRepository.save(item);
        try {
            if(itemRepository.findById(item.getItemID()).isEmpty()) {

                return "Item is saved";
            } else {
                return "Item already exists";
            }
        } catch (Exception e) {
            throw e;

        }
    }

        public Iterable<Item> readCarts() {
        return itemRepository.findAll();
    }

        @Transactional
        public String updateItem(Item item) {
        if (itemRepository.findById(item.getItemID()).isPresent()) {
            try {
                Item itemToUpdate = itemRepository.findById(item.getItemID()).get();
                itemToUpdate.setItemID(item.getItemID());
                return "Item info is updated";
            } catch (Exception e) {
                throw e;
            }
        } else {
            return "Item does not exist in DB";
        }
    }

        @Transactional
        public String deleteItem(Helmet helmet) {
        if (itemRepository.findById(helmet.getItemID()).isPresent()) {
            try {
                itemRepository.delete(helmet);
                return "Item has been deleted";
            } catch (Exception e) {
                throw e;
            }
        } else {
            return "Item does not exist in DB";
        }
    }
}
