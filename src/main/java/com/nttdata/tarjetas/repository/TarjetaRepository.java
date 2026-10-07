package com.nttdata.tarjetas.repository;

import com.nttdata.tarjetas.model.Tarjeta;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Guarda las tarjetas en una lista en memoria (no hay base de datos).
 * Al reiniciar la aplicación, los datos vuelven a su estado inicial.
 */
@Repository
public class TarjetaRepository {

    private final List<Tarjeta> tarjetas = new ArrayList<>();

    public TarjetaRepository() {
        tarjetas.add(new Tarjeta("4111000000000001", "Ana Torres", "CREDITO", 5000.00, "ACTIVA"));
        tarjetas.add(new Tarjeta("4111000000000002", "Luis Ramos", "DEBITO", 0.00, "ACTIVA"));
        tarjetas.add(new Tarjeta("4111000000000003", "María Quispe", "CREDITO", 12000.00, "BLOQUEADA"));
        tarjetas.add(new Tarjeta("4111000000000004", "Carlos Díaz", "CREDITO", 3000.00, "ACTIVA"));
        tarjetas.add(new Tarjeta("4111000000000005", "Rosa Huamán", "DEBITO", 0.00, "ACTIVA"));
    }

    /** Devuelve todas las tarjetas. */
    public List<Tarjeta> listarTodas() {
        return tarjetas;
    }

    /** Busca una tarjeta por su número. Si no existe, devuelve null. */
    public Tarjeta buscarPorNumero(String numero) {
        // Recorremos la lista con un ciclo for para buscar la tarjeta.
        for (Tarjeta tarjeta : tarjetas) {
            // Si el número coincide, devolvemos esa tarjeta.
            if (tarjeta.getNumero().equals(numero)) {
                return tarjeta;
            }
        }
        // Si no la encontramos, devolvemos null.
        return null;
    }

    /** Guarda una nueva tarjeta en la lista en memoria. */
    public void guardar(Tarjeta tarjeta) {
        // Agregamos la tarjeta al final de la lista.
        tarjetas.add(tarjeta);
    }
}
