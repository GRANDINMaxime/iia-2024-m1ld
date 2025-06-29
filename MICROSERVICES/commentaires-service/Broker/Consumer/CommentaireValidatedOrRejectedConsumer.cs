using commentaire_service.Context;
using commentaire_service.Models;
using commentaire_service.Models.Enums;
using commentaire_service.Broker.Event;
using commentaire_service.Broker.Command;
using Steeltoe.Messaging.RabbitMQ.Attributes;
using Steeltoe.Messaging.RabbitMQ.Core;
using Steeltoe.Messaging.RabbitMQ.Extensions;

namespace commentaire_service.Broker.Consumer;


public class CommentaireValidatedOrRejectedConsumer {
    private readonly IServiceProvider _services;

    public CommentaireValidatedOrRejectedConsumer(IServiceProvider services)
    {
        _services = services;
    }

    [DeclareQueueBinding(
        Name = "ms.commentaire.commentaire.validate",
        QueueName = "ms.commentaire.commentaire.validate",
        ExchangeName = "ms.commentaire",
        RoutingKey = "commentaire.validate")
    ]
    [RabbitListener(Binding = "ms.commentaire.commentaire.validate")]
     public void on(CommentaireValidatedEvent evt)
    {
        RabbitTemplate rabbitTemplate = _services.GetRabbitTemplate();

        using (var scope = _services.CreateScope())
        {
            var commentaireContext = scope.ServiceProvider.GetService<CommentaireContext>();
            Commentaire commentaire = commentaireContext.Commentaires.First(c => c.Id == evt.CommentaireId);

            commentaire.Etat = CommentaireEtat.OK;
            commentaireContext.SaveChanges();

            rabbitTemplate.ConvertAndSend("ms.commentaire", "commentaire.created", new CommentaireCreatedCommand
            {
                Id = evt.CommentaireId,
                Texte = commentaire.Texte,
                Note = (commentaire.NoteQualite + commentaire.NoteQualitePrix + commentaire.NoteFacilite) / 3,
                ProduitId = evt.ProduitId
            });
        }
    }

    [DeclareQueueBinding(
        Name = "ms.commentaire.commentaire.reject",
        QueueName = "ms.commentaire.commentaire.reject",
        ExchangeName = "ms.commentaire",
        RoutingKey = "commentaire.reject")
    ]
    [RabbitListener(Binding = "ms.commentaire.commentaire.reject")]
     public void on(CommentaireRejectedEvent evt)
    {
        using (var scope = _services.CreateScope())
        {
            var commentaireContext = scope.ServiceProvider.GetService<CommentaireContext>();
            Commentaire commentaire = commentaireContext.Commentaires.First(c => c.Id == evt.CommentaireId);

            commentaireContext.Commentaires.Remove(commentaire);
            commentaireContext.SaveChanges();
        }
    }
}