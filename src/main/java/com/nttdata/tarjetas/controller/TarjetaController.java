package com.nttdata.tarjetas.controller;

import com.nttdata.tarjetas.model.Tarjeta;
import com.nttdata.tarjetas.repository.TarjetaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * API REST de tarjetas. Cada método atiende un endpoint.
 */
@RestController
@RequestMapping("/api/v1/tarjetas")
public class TarjetaController {

    private final TarjetaRepository tarjetaRepository;

    public TarjetaController(TarjetaRepository tarjetaRepository) {
        this.tarjetaRepository = tarjetaRepository;
    }

    /** GET /api/v1/tarjetas -> lista todas las tarjetas (200). */
    @GetMapping
    public List<Tarjeta> listarTarjetas() {
        return tarjetaRepository.listarTodas();
    }

    /** GET /api/v1/tarjetas/{numero} -> devuelve la tarjeta si existe (200) o 404 si no existe. */
    @GetMapping(path = "/{numero}")
    public ResponseEntity<Tarjeta> buscarTarjetaPorNumero(@PathVariable("numero") String numero) {
        // Buscamos la tarjeta en el repositorio usando el número que vino en la URL.
        Tarjeta tarjeta = tarjetaRepository.buscarPorNumero(numero);

        // Si la tarjeta existe, devolvemos 200 OK con el objeto JSON.
        if (tarjeta != null) {
            return ResponseEntity.ok(tarjeta);
        }

        // Si no existe, devolvemos 404 Not Found.
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /** GET /api/v1/tarjetas/tipo/{tipo} -> devuelve solo las tarjetas del tipo solicitado (200) o 400 si el tipo es inválido. */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Tarjeta>> buscarTarjetasPorTipo(@PathVariable("tipo") String tipo) {
        // Si el tipo llega vacío o nulo, la petición es inválida -> 400.
        if (tipo == null || tipo.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // El tipo solo puede ser CREDITO o DEBITO -> 400.
        if (!"CREDITO".equalsIgnoreCase(tipo) && !"DEBITO".equalsIgnoreCase(tipo)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Recorremos todas las tarjetas y guardamos solo las del tipo solicitado.
        List<Tarjeta> tarjetasPorTipo = new ArrayList<>();
        List<Tarjeta> todasLasTarjetas = tarjetaRepository.listarTodas();

        for (Tarjeta tarjeta : todasLasTarjetas) {
            if (tipo.equalsIgnoreCase(tarjeta.getTipo())) {
                tarjetasPorTipo.add(tarjeta);
            }
        }

        // Si el tipo es válido, incluso si no hay resultados, devolvemos 200 con la lista vacía.
        return ResponseEntity.ok(tarjetasPorTipo);
    }

    /** POST /api/v1/tarjetas -> registra una tarjeta nueva. */
    @PostMapping
    public ResponseEntity<Tarjeta> registrarTarjeta(@RequestBody Tarjeta tarjeta) {
        // R1: el número debe tener 16 caracteres y el titular es obligatorio -> 400
        if (tarjeta == null || tarjeta.getNumero() == null || tarjeta.getNumero().length() != 16 || tarjeta.getTitular() == null || tarjeta.getTitular().trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // R2: el tipo solo puede ser CREDITO o DEBITO -> 400
        if (!"CREDITO".equalsIgnoreCase(tarjeta.getTipo()) && !"DEBITO".equalsIgnoreCase(tarjeta.getTipo())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // R3: una tarjeta CREDITO debe tener límite mayor a 0 -> 400
        if ("CREDITO".equalsIgnoreCase(tarjeta.getTipo()) && tarjeta.getLimite() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // R4: si el número ya existe -> 409
        Tarjeta tarjetaExistente = tarjetaRepository.buscarPorNumero(tarjeta.getNumero());
        if (tarjetaExistente != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // R5: la tarjeta se guarda con estado ACTIVA y responde 201 con la tarjeta.
        tarjeta.setEstado("ACTIVA");
        tarjetaRepository.guardar(tarjeta);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarjeta);
    }

    /** PUT /api/v1/tarjetas/{numero}/bloquear -> bloquea una tarjeta si existe. */
    @PutMapping("/{numero}/bloquear")
    public ResponseEntity<Tarjeta> bloquearTarjeta(@PathVariable("numero") String numero) {
        // Normalizamos el valor para evitar problemas con espacios.
        String numeroNormalizado = numero;

        if (numeroNormalizado != null) {
            numeroNormalizado = numeroNormalizado.trim();
        }

        // Si el número viene vacío, no existe.
        if (numeroNormalizado == null || numeroNormalizado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Buscamos la tarjeta en memoria.
        Tarjeta tarjeta = tarjetaRepository.buscarPorNumero(numeroNormalizado);

        // Si la tarjeta no existe, devolvemos 404.
        if (tarjeta == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // Si existe, la bloqueamos y devolvemos 200 OK.
        tarjeta.setEstado("BLOQUEADA");
        return ResponseEntity.ok(tarjeta);
    }
}
