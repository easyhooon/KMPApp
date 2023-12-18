import SwiftUI
import shared

struct ContentView: View {
    // @StateObject means lifecycyle of viewModel is managed by SwiftUI
    @StateObject
    var viewModel = HomeViewModel()

    var body: some View {
        VStack {
            if viewModel.response?.isSuccess() == true {
                List(viewModel.response?.getProducts().items ?? [], id: \.id) { element in
                    ProductView(product: element)
                }
            } else if viewModel.response?.isError() == true {
                VStack {
                    Spacer()
                    Text("\(viewModel.response?.getErrorMessage() ?? "Unknown Error")")
                        .font(.title3)
                        .fontWeight(.bold)
                    Spacer()
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
            } else if viewModel.response?.isLoading() == true {
                VStack {
                    Spacer()
                    ProgressView("Loading")
                        .progressViewStyle(CircularProgressViewStyle())
                        .padding()
                    Spacer()
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
            }
        }.task {
            // this block will be triggered when a view appears on the screen
            await viewModel.fetchData()
        }
    }
}

class HomeViewModel: ObservableObject  {
    // property with @Published annotation will be automatically updated when its value change
    @Published
    private(set) var response: RequestState? = nil
    
    // @MainActor annotation indicates that this fuction should be executed in Main thread
    // UI kit and Swift UI are not thread safe
    @MainActor
    func fetchData() async {
        for await requestState in ProductsApi().fetchProducts(limit: 10) {
            response = requestState
        }
    }
}
