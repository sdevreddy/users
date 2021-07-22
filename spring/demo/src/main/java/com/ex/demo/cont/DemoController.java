package com.ex.demo.cont;


import com.ex.demo.model.Employee;
import com.ex.demo.service.FirebaseInitializer;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class DemoController {
    @Autowired
    FirebaseInitializer db ;
    //@GetMapping("/getAllEmployees")
    @RequestMapping(method = RequestMethod.GET, path="/getAllEmployees")
    public List<Employee> getAllEmployees() throws ExecutionException, InterruptedException {

        List<Employee> emlist = new ArrayList<Employee>();
        CollectionReference employee =  db.getFirebase().collection("Employee");
        ApiFuture<QuerySnapshot> querySnapshot =  employee.get();
        for (DocumentSnapshot doc:querySnapshot.get().getDocuments()){

            emlist.add(doc.toObject(Employee.class));
        }
        return emlist ;
    }

    @PostMapping("/saveEmployee")
    public int saveEmployee(@RequestBody Employee employee) {
        CollectionReference employeeCR= db.getFirebase().collection("Employee");
        employeeCR.document(String.valueOf(employee.getId())).set(employee);
        return employee.getId();
    }

}