import org.elasticsearch.action.bulk.{BulkProcessor, BulkRequest, BulkResponse}
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.ImmutableSettings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.unit.{ByteSizeUnit, ByteSizeValue, TimeValue}
import org.slf4j.LoggerFactory

object EsClient {
  val logger = LoggerFactory.getLogger(this.getClass)

  val client: Client = {
    val settings = ImmutableSettings.settingsBuilder().put("cluster.name", "elasticsearch_lirsun").build()
    var client: TransportClient = new TransportClient(settings)
      client = client.addTransportAddress(new InetSocketTransportAddress("localhost", 9300))
    client
  }

  val loader = BulkProcessor.builder(
    client,
    new BulkProcessor.Listener() {
      override def beforeBulk(executionId: Long, request: BulkRequest): Unit = {}

      override def afterBulk(executionId: Long, request: BulkRequest, response: BulkResponse): Unit = {
        if(response.hasFailures) {
          logger.error(response.buildFailureMessage)
        }
      }
      override def afterBulk(executionId: Long, request: BulkRequest, failure: Throwable): Unit = {
        logger.error("Error",failure)
      }
    })
    .setBulkActions(1)
    .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.MB))
    .setFlushInterval(TimeValue.timeValueSeconds(5))
    .setConcurrentRequests(3)
    .build()
}
