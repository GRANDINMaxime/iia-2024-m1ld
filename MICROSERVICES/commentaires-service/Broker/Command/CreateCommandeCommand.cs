namespace commentaire_service.Broker.Command;


public class CreateCommentaireCommand
{
    public string CommentaireId { get; set; }
    public string ProduitId { get; set; }
}