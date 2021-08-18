package com.example.es.controller;

import com.example.es.model.Item;
import com.example.es.search.SearchRequestDTO;
import com.example.es.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Kyle Tong
 * @Date: 2021/8/6
 */
@RestController
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/index")
    public void index(@RequestBody final Item item) {
        itemService.index(item);
    }

    @GetMapping("/findById/{id}")
    public Item findById(@PathVariable final String id) {
        return itemService.getById(id);
    }

    @PostMapping("/search")
    public List<Item> search(@RequestBody final SearchRequestDTO searchRequestDTO) {
        return itemService.search(searchRequestDTO);
    }
}
