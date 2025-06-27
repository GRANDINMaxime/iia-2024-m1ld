namespace commentaire_service.Broker.Command;


public class CommentaireCreatedCommand
{
    public string Id { get; set; }
    public string Texte { get; set; }
    public int Note { get; set; }
    public string ProduitId { get; set; }
}