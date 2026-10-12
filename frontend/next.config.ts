import type { NextConfig } from "next";

// Dia chi Spring Boot backend. Proxy qua Next de trinh duyet goi cung origin -> khong can cau hinh CORS.
const BACKEND_URL = process.env.BACKEND_URL ?? "http://localhost:8080";

const nextConfig: NextConfig = {
  async rewrites() {
    return [
      {
        source: "/api/v1/:path*",
        destination: `${BACKEND_URL}/api/v1/:path*`,
      },
    ];
  },
};

export default nextConfig;
