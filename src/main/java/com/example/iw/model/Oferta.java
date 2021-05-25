package com.example.iw.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.time.format.DateTimeFormatter;
import lombok.Data;

@Entity
@Data
@NamedQueries({
	@NamedQuery(name="Oferta.byUser",
			query="SELECT o FROM Oferta o "
					+ "WHERE o.comprador.id = :userId OR o.vendedor.id = :userId"),
    @NamedQuery(name="Oferta.mejorPuja",
            query="SELECT MAX(precio) FROM Oferta WHERE tipo = 0 AND estado = 0 AND producto.id = :productoId"
            /*query="SELECT MAX(o.precio) FROM Oferta o WHERE tipo = 0 AND estado = 0 AND producto.id = :productoId AND NOT o.comprador.id =:userId"*/),
    @NamedQuery(name="Oferta.mPuja",
            query="SELECT o FROM Oferta o " + 
            "WHERE o.precio = (SELECT MAX(precio) FROM Oferta WHERE tipo = 0 AND estado = 0 AND producto.id = :productoId AND NOT comprador.id =:userId)"),
    @NamedQuery(name="Oferta.menorPrecio",
            query="SELECT MIN(precio) FROM Oferta WHERE tipo = 1 AND estado = 0 AND producto.id = :productoId"
            /*query="SELECT MIN(o.precio) FROM Oferta o WHERE tipo = 1 AND estado = 0 AND producto.id = :productoId AND NOT o.vendedor.id =:userId"*/),
    @NamedQuery(name="Oferta.mPrecio",
            query="SELECT o FROM Oferta o " + 
            "WHERE o.precio = (SELECT MIN(precio) FROM Oferta WHERE tipo = 1 AND estado = 0 AND producto.id = :productoId AND NOT vendedor.id =:userId)"),
    @NamedQuery(name="Oferta.pujas",
            query="SELECT o FROM Oferta o WHERE tipo = 0 AND estado = 0 AND producto.id = :productoId ORDER BY precio DESC"),
    @NamedQuery(name="Oferta.precios", 
            query="SELECT o FROM Oferta o WHERE tipo = 1 AND estado = 0  AND producto.id = :productoId ORDER BY precio"),

    @NamedQuery(name="Oferta.pujasUser",
            query="SELECT o FROM Oferta o WHERE tipo = 0 AND estado = 0 AND comprador.id = :userId"),
    @NamedQuery(name="Oferta.preciosUser", 
            query="SELECT o FROM Oferta o WHERE tipo = 1 AND estado = 0 AND vendedor.id = :userId"),

    @NamedQuery(name="Oferta.comprasUser",
            query="SELECT o FROM Oferta o WHERE fechaTransaccion IS NOT NULL AND comprador.id = :userId"),
    @NamedQuery(name="Oferta.ventasUser", 
            query="SELECT o FROM Oferta o WHERE fechaTransaccion IS NOT NULL AND vendedor.id = :userId"),
    @NamedQuery(name="Oferta.transaction", 
            query="SELECT o FROM Oferta o WHERE fechaTransaccion IS NOT NULL AND producto.id = :productoId ORDER BY fechaTransaccion DESC")
        
        })
            
public class Oferta {
    @Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
   
    public enum Tipo{
        PUJA,
        PRECIO
    };

    public enum Estado{
        PENDIENTE, 
        TRANSACCION_REALIZADA,
        EN_ENVIO,
        RECIBIDO
    };

    private Tipo tipo; 
    private BigDecimal precio;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaExpiracion;
    private LocalDateTime fechaTransaccion; //si este campo es igual a NULL significa que aún la transacción no se ha completado
    private Estado estado;
    

    @ManyToOne
    private Usuario comprador; //si este campo es igual a NULL significa que aún la transacción no se ha completado
   
    @ManyToOne
    private Usuario vendedor; //si este campo es igual a NULL significa que aún la transacción no se ha completado

    @ManyToOne
    private Producto producto;

    @Override
    public String toString() {
        return "Oferta #" + id;
    }

    public Oferta(){

    }
}

