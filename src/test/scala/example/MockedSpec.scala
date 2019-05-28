package example.application

import example.domain.{ Asset, AssetId, AssetRepository }
import org.scalatest._
import scalaz.zio.IO
import scalaz.zio.DefaultRuntime
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito
import example.domain.PortfolioAssetRepository
import example.domain.PortfolioId
import example.domain.PortfolioAsset

class MockedSpec extends FlatSpec with Matchers with DefaultRuntime with MockitoSugar {
  
  val mockedAssetRepository: AssetRepository = new AssetRepository() {
    val assetRepository: AssetRepository.Service = mock[AssetRepository.Service]
  }

  val mockedPortfolioAssetRepository: PortfolioAssetRepository = new PortfolioAssetRepository() {
    val portfolioAssetRepository: PortfolioAssetRepository.Service = mock[PortfolioAssetRepository.Service]
  }

  "The Hello object" should "say hello" in {
    Mockito.when(mockedAssetRepository.assetRepository.getAll).thenReturn(IO {
      List.empty
    } refineOrDie {
      case e: Exception => e
    })
    val result = this.unsafeRun(ApplicationService.getAssets.provide(mockedAssetRepository))
    result shouldEqual(List.empty)
  }
 
  "ApplicationService.getPortfolio" should "return a portfolio" in {
    val portfolioId = PortfolioId(1)

    Mockito.when(mockedPortfolioAssetRepository.portfolioAssetRepository.getByPortfolioId(portfolioId)).thenReturn(IO {
      List(
        PortfolioAsset(portfolioId, AssetId(1), BigDecimal(1)), 
        PortfolioAsset(portfolioId, AssetId(2), BigDecimal(1)))
    } refineOrDie {
      case e: Exception => e
    })

    Mockito.when(mockedAssetRepository.assetRepository.getByIds(Set(AssetId(1), AssetId(2)))).thenReturn(IO {
      List(
        Asset(AssetId(1), "PLN", BigDecimal(1)), 
        Asset(AssetId(2), "USD", BigDecimal(1)))
    } refineOrDie {
      case e: Exception => e
    })

    
  }

}