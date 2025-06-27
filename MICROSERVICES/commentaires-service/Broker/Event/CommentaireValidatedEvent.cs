namespace commentaire_service.Broker.Event;

public class CommentaireValidatedEvent
{
    public string CommentaireId { get; set; }
    public string ProduitId { get; set; }
}