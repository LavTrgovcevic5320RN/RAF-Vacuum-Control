package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.models.Vacuum;
import com.example.demo.models.VacuumStatus;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.VacuumRepository;
import com.example.demo.requests.VacuumRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class VacuumService {

    @Autowired
    private ErrorMessageService errorMessageService;

    @Autowired
    private VacuumRepository vacuumRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    public List<Vacuum> searchVacuums(String name, List<String> status, Date dateFrom, Date dateTo, String email) {
        List<Vacuum> activeVacuums = vacuumRepository.findVacuumsForUser(email);
        List<Vacuum> filteredVacuums = activeVacuums.stream()
                .filter(vacuum -> name == null || vacuum.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(vacuum -> status == null || status.isEmpty() || status.contains(vacuum.getStatus().toString()))
                .filter(vacuum -> (dateFrom == null || vacuum.getCreatedAt().after(dateFrom)))
                .filter(vacuum -> (dateTo == null || vacuum.getCreatedAt().before(dateTo)))
                .collect(Collectors.toList());

        System.out.println(filteredVacuums);
        return filteredVacuums;
    }

    public Vacuum addVacuum(VacuumRequest vacuumRequest, String loggedInUsername) {
        User loggedInUser = userRepository.findByEmail(loggedInUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Vacuum newVacuum = new Vacuum();
        newVacuum.setName(vacuumRequest.getName());
        newVacuum.setStatus(VacuumStatus.STOPPED);
        newVacuum.setAddedBy(loggedInUser);
        newVacuum.setActive(true);
        newVacuum.setCreatedAt(new Date());
        newVacuum.setDeleted(false);
        newVacuum.setRefCnt(0);

        return vacuumRepository.save(newVacuum);
    }

    @Transactional
    public boolean removeVacuum(String user, Long vacuumId) {
        this.entityManager.clear();
        try{
            Optional<Vacuum> vac = vacuumRepository.findVacuumById(vacuumId);

            if(!vac.isPresent()) {
                //pisati error u bazu
                this.errorMessageService.createError(null, "REMOVE", "Vacuum not found",new Date(), user);
                return false;
            }

            Vacuum vacuum = vac.get();

            if(!vacuum.getAddedBy().getEmail().equals(user)){
                this.errorMessageService.createError(null, "REMOVE", "Unauthorized action",new Date(), user);
                return false;
            }

            vacuum.setStatus(VacuumStatus.STOPPED);
            vacuum.setDeleted(true);

            this.vacuumRepository.saveAndFlush(vacuum);
            System.out.println("Vacuum updated");
            return true;
        }catch(Exception e) {
            e.printStackTrace();
            this.errorMessageService.createError(null, "REMOVE", e.getMessage(),new Date(), user);
            return false;
        }
    }

    @Async
    @Transactional
    public CompletableFuture<Long> startVacuum(String email, Long vacuumId) {
        startVacuumAsync(email, vacuumId);
        return CompletableFuture.completedFuture(vacuumId);
    }

    public void startVacuumAsync(String email, Long vacuumId) {
        try{
            this.entityManager.clear();
            Optional<Vacuum> opt = this.vacuumRepository.findVacuumForStart(vacuumId);

            if (!opt.isPresent()) {
                this.errorMessageService.createError(null, "START", "Vacuum not found",new Date(), email);
                return;
            }

            Vacuum vac = opt.get();

            if(!vac.getAddedBy().getEmail().equals(email)) {
                this.errorMessageService.createError(vac, "START", "Unauthorized action",new Date(), email);
                return;
            }

            if(vac.getStatus() == VacuumStatus.RUNNING) {
                this.errorMessageService.createError(vac, "START", "Vacuum already started",new Date(), email);
                return;
            }


            Thread.sleep(15000);
            vac.setStatus(VacuumStatus.RUNNING);
            vac.setRefCnt(vac.getRefCnt() + 1);

            this.vacuumRepository.saveAndFlush(vac);
            System.out.println("Vacuum started: " + vacuumId);
        }catch (InterruptedException e) {
            this.errorMessageService.createError(null, "REMOVE", e.getMessage(),new Date(), email);
            Thread.currentThread().interrupt();
        }
    }

    @Async
    @Transactional
    public CompletableFuture<Long> stopVacuum(String user, Long vacuumId) {
        stopVacuumAsync(user, vacuumId);
        return CompletableFuture.completedFuture(vacuumId);
    }

    private void stopVacuumAsync(String email, Long vacuumId) {
        try{
            this.entityManager.clear();
            Optional<Vacuum> opt = this.vacuumRepository.findVacuumForStop(vacuumId);

            if (!opt.isPresent()) {
                this.errorMessageService.createError(null, "STOP", "Vacuum not found",new Date(), email);
                return;
            }

            Vacuum vac = opt.get();

            if(!vac.getAddedBy().getEmail().equals(email)) {
                this.errorMessageService.createError(vac, "STOP", "Unauthorized action",new Date(), email);
                return;
            }

            if(vac.getStatus() == VacuumStatus.STOPPED) {
                this.errorMessageService.createError(vac, "STOP", "Vacuum already stopped",new Date(), email);
                return;
            }

            Thread.sleep(15000);
            vac.setStatus(VacuumStatus.STOPPED);

            if(vac.getRefCnt() % 3 == 0) {
                startDischargeAsync(email, vacuumId);
                vac.setRefCnt(vac.getRefCnt() + 1);

                return;
            }

            vac.setRefCnt(vac.getRefCnt() + 1);
            this.vacuumRepository.saveAndFlush(vac);
            System.out.println("Vacuum stopped: " + vacuumId);
        }catch (InterruptedException e) {
            this.errorMessageService.createError(null, "STOP", e.getMessage(),new Date(), email);
            Thread.currentThread().interrupt();
        }
    }

    @Async
    @Transactional
    public CompletableFuture<Long> dischargeVacuum(String email, Long vacuumId) {
        startDischargeAsync(email, vacuumId);

        return CompletableFuture.completedFuture(vacuumId);
    }

    private void startDischargeAsync(String email, Long vacuumId) {
        try {
            this.entityManager.clear();

            Optional<Vacuum> opt = this.vacuumRepository.findVacuumForStop(vacuumId);

            if (!opt.isPresent()) {
                this.errorMessageService.createError(null, "DISCHARGE", "Vacuum not found",new Date(), email);
                return;
            }


            Vacuum vacuum = opt.get();

            if(vacuum.getStatus() == VacuumStatus.STOPPED) {
                this.errorMessageService.createError(vacuum, "DISCHARGE", "Can't discharge a stopped vacuum!",new Date(), email);
                return;
            }

            Thread.sleep(15000);

            vacuum.setStatus(VacuumStatus.DISCHARGING);
            this.vacuumRepository.saveAndFlush(vacuum);
            Thread.sleep(15000);

            vacuum.setStatus(VacuumStatus.STOPPED);
            this.vacuumRepository.saveAndFlush(vacuum);

            System.out.println("Vacuum discharged: " + vacuumId);
        }catch (InterruptedException e) {
            this.errorMessageService.createError(null, "DISCHARGE", e.getMessage(),new Date(), email);
            e.printStackTrace();
        }
    }
}

