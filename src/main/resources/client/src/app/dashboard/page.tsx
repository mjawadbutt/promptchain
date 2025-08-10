import React from "react";

export default function DashboardPage() {
  return (
    <div>
      <h1 className="text-4xl font-bold mb-6">Dashboard</h1>
      <hr className="border-gray-700 mb-8" />
      <section className="mb-10">
        <h2 className="text-2xl font-bold mb-2">Comprehensive insights into your LLM application's performance.</h2>
        <p className="mb-4 text-gray-300">
          Monitor real-time request latencies, error rates, token consumption, and quality metrics. Our platform supports multiple metrics across major foundation models (OpenAI, Anthropic, Cohere, Bedrock), vector databases (Pinecone, Weaviate)
        </p>
        <div className="flex gap-4">
          <button className="bg-green-600 text-white px-6 py-2 rounded-full font-semibold hover:bg-green-700">
            Connect your Application
          </button>
          <button className="border border-green-600 text-green-400 px-6 py-2 rounded-full font-semibold hover:bg-green-800 hover:text-white">
            Read Documentation
          </button>
        </div>
      </section>
      <section className="grid grid-cols-2 md:grid-cols-3 gap-6 mt-10">
        {/* Example dashboard cards */}
        <div className="bg-midnight-navy rounded-lg p-6">
          <h3 className="font-semibold mb-2">Models</h3>
          <ul className="text-sm text-gray-400">
            <li>claude-3-opus-20240229: 10,699</li>
            <li>gpt-4o: 18,475</li>
            <li>command-r-plus: 28,777</li>
          </ul>
        </div>
        <div className="bg-midnight-navy rounded-lg p-6">
          <h3 className="font-semibold mb-2">Total Price</h3>
          <div className="text-3xl font-bold text-pink-400 mb-2">$2000</div>
          <ul className="text-sm text-gray-400">
            <li>claude-3-opus: $6044</li>
            <li>gpt-4o: $1240</li>
            <li>command-r-plus: $200</li>
          </ul>
        </div>
        <div className="bg-midnight-navy rounded-lg p-6">
          <h3 className="font-semibold mb-2">Tokens Per Minute</h3>
          <div className="h-20 bg-gray-800 rounded mb-2 flex items-center justify-center text-gray-500 text-xs">
            {/* Placeholder for chart */}
            Chart
          </div>
        </div>
        <div className="bg-midnight-navy rounded-lg p-6">
          <h3 className="font-semibold mb-2">Monitored Tasks</h3>
          <div className="text-2xl font-bold text-green-400 mb-2">16 Tasks</div>
          <div className="text-sm text-gray-400">5 Not monitored, 11 Monitored</div>
        </div>
        <div className="bg-midnight-navy rounded-lg p-6 col-span-2">
          <h3 className="font-semibold mb-2">Daily Cost</h3>
          <div className="h-20 bg-gray-800 rounded mb-2 flex items-center justify-center text-gray-500 text-xs">
            {/* Placeholder for chart */}
            Chart
          </div>
        </div>
      </section>
    </div>
  )
}